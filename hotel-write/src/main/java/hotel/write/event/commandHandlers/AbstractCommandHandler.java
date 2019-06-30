package hotel.write.event.commandHandlers;


import hotel.write.clients.UserReadClient;
import hotel.write.domain.Hotel;
import hotel.write.domain.interfaces.HotelsInterface;
import hotel.write.event.commands.HotelUpdateCommand;
import hotel.write.event.events.EventRoot;
import hotel.write.event.kafka.EventPublisher;
import hotel.write.implementations.ApplicationConfiguration;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * AbstractCommandHandler is an internal listener for internal application events
 * It is triggered by KafkaListener which republishes dynamically received remote command object
 * This is then extended by relevant application commandHanders all within this folder structure.
 *
 */

@Singleton
public abstract class AbstractCommandHandler<E> implements HotelsInterface, ApplicationEventListener<E> {


    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;



    public final UserReadClient userReadClient;

    private final EmbeddedServer embeddedServer;
    protected static final String topic = "hotelRead";
    private final EventPublisher eventPublisher;


    public AbstractCommandHandler(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration,
                                  EventPublisher eventPublisher, EmbeddedServer embeddedServer, UserReadClient userReadClient) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
        this.eventPublisher = eventPublisher;
        this.embeddedServer = embeddedServer;
        this.userReadClient=userReadClient;
    }
    @Transactional
    public void persistToDb(Object object) {
        entityManager.persist(object);
    }
    @Transactional
    public void merge(Object object) {
        entityManager.merge(object);
    }
    @Transactional
    public void removeFromDb(Object object) {
        entityManager.remove(object);
    }

    @Transactional
    public void updateDb(HotelUpdateCommand cmd) {
        findById(cmd.getId()).ifPresent(hotel -> entityManager.createQuery("UPDATE Hotel h  SET name = :name, code = :code, email = :email, phone = :phone  where id = :id")
                .setParameter("name", cmd.getName())
                .setParameter("id", cmd.getId())
                .setParameter("code", cmd.getCode())
                .setParameter("phone", cmd.getPhone())
                .setParameter("email", cmd.getEmail())
                .executeUpdate()
        );
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
