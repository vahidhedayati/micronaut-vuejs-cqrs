package hotel.read.event.eventHandlers;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.domain.Hotel;
import hotel.read.event.events.HotelUpdated;
import hotel.read.implementation.ApplicationConfiguration;
import hotel.read.implementation.SortingAndOrderArguments;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This is our eventHandler it is completed commands that were sent out as an event
 *
 * "only difference currently being naming convention of command to event"
 *
 * This is our base eventHandler when KafkaEventListener picks up a new event it republishes dynamically
 * received remote object
 *
 * This then splits or has dynamic events per received event Type that listens in locally and does what is needed
 *
 * @param <E>
 */
@Singleton
public abstract class AbstractEventHandler<E>  implements ApplicationEventListener<E> {


    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;



    public AbstractEventHandler(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }


    @Transactional(readOnly = true)
    public Optional<Hotel> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Hotel.class, id));
    }
    @Transactional
    public void merge(Object object) {
        entityManager.merge(object);
    }
    @Transactional
    public void persistToDb(Object object) {
        entityManager.persist(object);
    }
    @Transactional
    public void removeFromDb(Object object) {
        entityManager.remove(object);
    }
    @Transactional
    public void updateDb(HotelUpdated cmd) {
        findById(cmd.getId()).ifPresent(hotel -> entityManager.createQuery("UPDATE Hotel h  SET name = :name, code = :code, email = :email, phone = :phone  where id = :id")
                .setParameter("name", cmd.getName())
                .setParameter("id", cmd.getId())
                .setParameter("code", cmd.getCode())
                .setParameter("phone", cmd.getPhone())
                .setParameter("email", cmd.getEmail())
                .executeUpdate()
        );
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


    @Transactional
    public void save(Hotel hotel) {
        if (hotel!=null) {
            if (!findByCode(hotel.getCode()).isPresent()) {
                entityManager.persist(hotel);
            }
        }
    }


    @Transactional
    public Optional<Hotel> findByCode(String code) {
        return entityManager
                .createQuery("from Hotel h where h.code = :code", Hotel.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }

}
