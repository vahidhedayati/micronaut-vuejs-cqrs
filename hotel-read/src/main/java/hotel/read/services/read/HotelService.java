package hotel.read.services.read;


import hotel.read.adaptors.models.HotelModel;
import hotel.read.commands.*;
import hotel.read.domain.Hotel;
import hotel.read.domain.HotelRooms;
import hotel.read.domain.interfaces.HotelsInterface;
import hotel.read.implementation.ApplicationConfiguration;
import hotel.read.implementation.SortingAndOrderArguments;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
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

        //SELECT new map (h.code as code, h.name as name, h.id as id, h.hotelRooms as hotelRooms)
        System.out.println("Doing search 123 ");
        String countQueryString= "select count(h) FROM Hotel as  h ";
        String qlString = "FROM Hotel as  h ";
        if (args.getName().isPresent()) {
            qlString += " where lower(h.name) like (:name) ";
            countQueryString += " where lower(h.name) like (:name) ";
        }
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY h." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        // System.out.println("Query "+qlString);
        TypedQuery<Hotel> query;
        TypedQuery<Long> countQuery;
        //Long countQuery=0L;
        if (args.getName().isPresent()) {
            query=entityManager.createQuery(qlString, Hotel.class).setParameter("name",'%'+args.getName().get().toLowerCase()+'%');
            countQuery=entityManager.createQuery(countQueryString, Long.class).setParameter("name",'%'+args.getName().get().toLowerCase()+'%');
        } else {
            query=entityManager.createQuery(qlString, Hotel.class);
            countQuery=entityManager.createQuery(countQueryString, Long.class);
        }

        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);
        //countQuery.setMaxResults(1);
        HotelModel model = new HotelModel();
        //System.out.println(" "+(countQuery.getFirstResult())+"1 sss ssss");
        model.setInstanceList(Optional.of(query.getResultList()));//.flatMap(hotel -> {}));
        //System.out.println(" "+(countQuery.getSingleResult())+"0 sss ssss");
        model.setInstanceTotal(countQuery.getSingleResult());

        model.setNumberOfPages(model.getInstanceTotal()/args.getMax().get());
        System.out.println(" SEARCH ::: "+model.getInstanceTotal()+" "+model.getNumberOfPages()+" "+model.getInstanceList());
        return Optional.of(model); //Single.just(model);
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

    @Override
    @Transactional
    public void save(HotelCreatedCommand cmd) {
        System.out.println("Doing READ  HotelCreatedCommand save "+cmd.getName());
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
        System.out.println("Doing hotel save "+cmd.getName());
        save(new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail()));
    }

    @Override
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
