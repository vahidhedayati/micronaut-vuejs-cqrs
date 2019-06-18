package hotel.write.services.write;


import hotel.write.commands.HotelDeleteCommand;
import hotel.write.commands.HotelUpdateCommand;
import hotel.write.domain.Hotel;
import hotel.write.domain.interfaces.HotelsEventInterface;
import hotel.write.implementations.ApplicationConfiguration;
import hotel.write.kafka.EventPublisher;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//imlpements Hotels

@Singleton
public class HotelDb implements HotelsEventInterface {



    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    private final EventPublisher publisher;
    private final EmbeddedServer embeddedServer;

    public HotelDb(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration, EventPublisher publisher, EmbeddedServer embeddedServer) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
        this.publisher = publisher;
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
        System.out.println("Doing hotel delete "+cmd.getId());
        findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }
    @Override
    @Transactional
    public void update(HotelUpdateCommand cmd) {
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

    @Transactional
    public void add(List<Hotel> hotels) {
        for ( final Hotel hotel : hotels ) {
            entityManager.persist(hotel);
        }
    }

    @Transactional
    public void save(Hotel hotel) {

        if (hotel!=null) {
            System.out.println("Hotel is not null - Doing hotel Add "+hotel.getCode());
            if (!findByCode(hotel.getCode()).isPresent()) {
               // System.out.println("Error - hotel already added "+hotel.getName());
            //} else {
                System.out.println("Doing hotel Add "+hotel.getCode());
                entityManager.persist(hotel);
            }

            // System.out.println("bus.handleCommand new CreateHotelCommand");
        } else {
           System.out.println("Hotel not being added - HOTEL is null --- ERROR ----- ");
        }

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
