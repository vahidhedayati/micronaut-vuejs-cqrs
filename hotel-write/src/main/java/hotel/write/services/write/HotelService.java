package hotel.write.services.write;


import hotel.write.clients.UserReadClient;
import hotel.write.commands.*;
import hotel.write.domain.Hotel;
import hotel.write.domain.HotelRooms;
import hotel.write.domain.interfaces.HotelsInterface;
import hotel.write.implementations.ApplicationConfiguration;
import hotel.write.kafka.EventPublisher;
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

    @Override
    @Transactional
    public void delete(HotelDeleteCommand cmd) {
        HotelDeletedCommand cmd1 = new HotelDeletedCommand(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);
        findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }

    @Override
    @Transactional
    public void update(HotelUpdateCommand cmd) {
        HotelUpdatedCommand cmd1 = new HotelUpdatedCommand(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        findById(cmd.getId()).ifPresent(hotel -> entityManager.createQuery("UPDATE Hotel h  SET name = :name, code = :code, email = :email, phone = :phone  where id = :id")
                .setParameter("name", cmd.getName())
                .setParameter("id", cmd.getId())
                .setParameter("code", cmd.getCode())
                .setParameter("phone", cmd.getPhone())
                .setParameter("email", cmd.getEmail())
                .executeUpdate()
        );
    }

    /**
     * This publishes to hotelRead Topic - picked up by hotelRead microservice
     * @param cmd
     */
    public void publishEvent(Command cmd) {
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
    public void save(HotelCreateCommand cmd) {
        HotelCreatedCommand cmd1 = new HotelCreatedCommand(cmd);
        cmd1.setUpdateUserName(userReadClient.findById(cmd.getUpdateUserId()).map(u->u.getUsername()));
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        Hotel hotel = new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(),cmd.getUpdateUserId(),cmd.getLastUpdated());
        List<HotelRooms> hotelRooms = new ArrayList<>();
        if (!findByCode(hotel.getCode()).isPresent()) {
            entityManager.persist(hotel);
            for (HotelRoomsCreateCommand rmc  : cmd.getHotelRooms() ) {
                HotelRooms hotelRooms1 = new HotelRooms(hotel,rmc.getRoomType(),rmc.getPrice(), rmc.getStockTotal());
                hotelRooms.add(hotelRooms1);
            }
            hotel.setHotelRooms(hotelRooms);
            entityManager.persist(hotel);
        }
    }

    @Override
    @Transactional
    public void save(HotelSaveCommand cmd) {
        HotelSavedCommand cmd1 = new HotelSavedCommand(cmd);
        cmd1.setUpdateUserName(userReadClient.findById(cmd.getUpdateUserId()).map(u->u.getUsername()));
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        save(new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail()));
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
