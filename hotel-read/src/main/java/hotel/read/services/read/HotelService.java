package hotel.read.services.read;


import hotel.read.adaptors.models.HotelModel;
import hotel.read.commands.*;
import hotel.read.domain.Hotel;
import hotel.read.domain.HotelRooms;
import hotel.read.domain.interfaces.HotelsInterface;
import hotel.read.implementation.ApplicationConfiguration;
import hotel.read.implementation.SortingAndOrderArguments;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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



    public HotelService(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Hotel.class, id));
    }


    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name", "code", "lastUpdated", "phone","email");

    @Transactional(readOnly = true)
    public Optional<HotelModel> findAll(@NotNull SortingAndOrderArguments args) {
        String countQueryString= "select count(h) FROM Hotel as  h ";
        String qlString = "FROM Hotel as  h ";
        if (args.getName().isPresent()) {
            qlString += " where lower(h.name) like (:name) ";
            countQueryString += " where lower(h.name) like (:name) ";
        }
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY h." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Hotel> query;
        TypedQuery<Long> countQuery;
        if (args.getName().isPresent()) {
            query=entityManager.createQuery(qlString, Hotel.class).setParameter("name",'%'+args.getName().get().toLowerCase()+'%');
            countQuery=entityManager.createQuery(countQueryString, Long.class).setParameter("name",'%'+args.getName().get().toLowerCase()+'%');
        } else {
            query=entityManager.createQuery(qlString, Hotel.class);
            countQuery=entityManager.createQuery(countQueryString, Long.class);
        }

        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);
        HotelModel model = new HotelModel();
        model.setInstanceList(Optional.of(query.getResultList()));
        model.setInstanceTotal(countQuery.getSingleResult());
        model.setNumberOfPages(model.getInstanceTotal()/args.getMax().get());
        return Optional.of(model);
    }

    @Override
    @Transactional
    public void delete(HotelDeletedCommand cmd) {
        findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }

    @Override
    @Transactional
    public void update(HotelUpdatedCommand cmd) {
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

    @Override
    @Transactional
    public void save(HotelCreatedCommand cmd) {
        Hotel hotel = new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(),cmd.getUpdateUserId(),cmd.getLastUpdated(),cmd.getUpdateUserName().get());
        List<HotelRooms> hotelRooms = new ArrayList<>();
        if (!findByCode(hotel.getCode()).isPresent()) {
            entityManager.persist(hotel);
            if (cmd.getHotelRooms()!=null) {
                for (HotelRoomsCreateCommand rmc  : cmd.getHotelRooms() ) {
                    HotelRooms hotelRooms1 = new HotelRooms(hotel,rmc.getRoomType(),rmc.getPrice(), rmc.getStockTotal());
                    hotelRooms.add(hotelRooms1);
                }
                hotel.setHotelRooms(hotelRooms);
            }
            entityManager.persist(hotel);
        }

    }

    @Override
    @Transactional
    public void save(HotelSavedCommand cmd) {
        save(new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(), cmd.getUpdateUserId(), cmd.getUpdateUserName().get()));
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

    @Override
    public Hotel getByCode(String code) {
        return findByCode(code).orElseThrow(() -> new RuntimeException("Hotel not found"));
    }
}
