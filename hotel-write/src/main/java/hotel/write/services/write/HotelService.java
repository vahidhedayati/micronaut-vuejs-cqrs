package hotel.write.services.write;


import hotel.write.clients.UserReadClient;
import hotel.write.event.commands.*;
import hotel.write.domain.Hotel;
import hotel.write.domain.HotelRooms;
import hotel.write.domain.interfaces.HotelsInterface;
import hotel.write.event.events.*;
import hotel.write.implementations.ApplicationConfiguration;
import hotel.write.event.kafka.EventPublisher;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Singleton
public class HotelService implements HotelsInterface {


    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;
    private final UserReadClient userReadClient;

    private final EmbeddedServer embeddedServer;
    protected static final String topic = "hotelRead";
    private final EventPublisher eventPublisher;

    public HotelService(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration,
                        EventPublisher eventPublisher, EmbeddedServer embeddedServer,UserReadClient userReadClient) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
        this.eventPublisher = eventPublisher;
        this.embeddedServer = embeddedServer;
        this.userReadClient=userReadClient;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Hotel.class, id));
    }


    /**
     * This publishes to hotelRead Topic - picked up by hotelRead microservice
     * @param cmd
     */
    public void publishEvent(EventRoot cmd) {
        eventPublisher.publish(embeddedServer,topic,cmd);
    }

    @Transactional
    public void add(List<Hotel> hotels) {
        for ( final Hotel hotel : hotels ) {
            entityManager.persist(hotel);
        }
    }

    @Override
    @Transactional
    public void save(Hotel hotel) {
        if (hotel!=null) {
            if (!findByCode(hotel.getCode()).isPresent()) {
                entityManager.persist(hotel);
            }
        }
    }

    @Transactional
    @Override
    public Optional<Hotel> findByCode(String code) {
        return entityManager
                .createQuery("from Hotel h where h.code = :code", Hotel.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }
}
