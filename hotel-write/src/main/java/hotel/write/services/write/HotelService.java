package hotel.write.services.write;


import hotel.write.commands.*;
import hotel.write.domain.Hotel;
import hotel.write.domain.HotelRooms;
import hotel.write.domain.interfaces.HotelsInterface;
import hotel.write.implementations.ApplicationConfiguration;
import hotel.write.kafka.EventPublisher;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Singleton
public class HotelService implements HotelsInterface {


    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;


    private final EmbeddedServer embeddedServer;
    protected static final String topic = "hotelRead";
    private final EventPublisher eventPublisher;

    public HotelService(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration, EventPublisher eventPublisher, EmbeddedServer embeddedServer) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
        this.eventPublisher = eventPublisher;
        this.embeddedServer = embeddedServer;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Hotel.class, id));
    }

    @Override
    @Transactional
    public void delete(HotelDeleteCommand cmd) {
        publishEvent(cmd);
        System.out.println("Doing hotel delete "+cmd.getId());
        findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }

    @Override
    @Transactional
    public void update(HotelUpdateCommand cmd) {
        publishEvent(cmd);
        System.out.println("Doing hotel update "+cmd.getName());
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
    @Transactional
    public void save(HotelCreatedCommand cmd) {
        publishEvent(cmd);
        Hotel hotel = new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(),cmd.getUpdateUserId(),cmd.getLastUpdated());
        List<HotelRooms> hotelRooms = new ArrayList<>();
        if (!findByCode(hotel.getCode()).isPresent()) {
            for (HotelRoomsCreateCommand rmc  : cmd.getHotelRooms() ) {
                HotelRooms hotelRooms1 = new HotelRooms(hotel,rmc.getRoomType(),rmc.getPrice(), rmc.getStockTotal());
                hotelRooms.add(hotelRooms1);
            }
            save(new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(),cmd.getUpdateUserId(),hotelRooms,cmd.getLastUpdated()));
        }
    }

    @Transactional
    public void save(HotelSaveCommand cmd) {
        System.out.println("Doing hotel save "+cmd.getName());
        publishEvent(cmd);
        save(new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail()));
    }

    @Transactional
    public void save(Hotel hotel) {
        if (hotel!=null) {
           // System.out.println("Hotel is not null - Doing hotel Add "+hotel.getCode());
            if (!findByCode(hotel.getCode()).isPresent()) {
                System.out.println("Doing hotel Add "+hotel.getCode());
                entityManager.persist(hotel);
            }
        }
        //else {
        //   System.out.println("Hotel not being added - HOTEL is null --- ERROR ----- ");
        //}
    }


    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name", "code", "lastUpdated", "phone","email");




    // public Single<List<Hotel>> listAll(Map input) {

    // }


    @Transactional
    @Override
    public Optional<Hotel> findByCode(String code) {
        return entityManager
                .createQuery("from Hotel h where h.code = :code", Hotel.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Hotel getByCode(String code) {
        return findByCode(code).orElseThrow(() -> new RuntimeException("Hotel not found"));
    }


}
