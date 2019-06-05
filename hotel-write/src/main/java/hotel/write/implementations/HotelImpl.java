package hotel.write.implementations;

import hotel.write.domain.Hotel;
import hotel.write.domain.interfaces.Hotels;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class HotelImpl implements Hotels {



    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;



    public HotelImpl(@CurrentSession EntityManager entityManager,
                     ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }



    @Override
    @Transactional
    public Hotel save(@NotBlank String code, @NotBlank String name) {
        Hotel hotel = new Hotel(code,name);
        entityManager.persist(hotel);
        return hotel;
    }

    @Transactional(readOnly = true)
    public Optional<Hotel> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Hotel.class, id));
    }
    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        findById(id).ifPresent(hotel -> entityManager.remove(hotel));
    }
    @Transactional
    public Optional<Hotel> findByCode(String code) {
        return entityManager
                .createQuery("from Hotel h where h.code = :code", Hotel.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }


    @Override
    @Transactional
    public int update(@NotNull Long id, @NotBlank String name, @NotBlank String code,@NotBlank  String phone,@NotBlank String email) {
        return entityManager.createQuery("UPDATE Hotel h  SET name = :name, code = :code, email = :email, phone = :phone  where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .setParameter("code", code)
                .setParameter("phone", phone)
                .setParameter("email", email)
                .executeUpdate();
    }




    @Transactional
    @Override
    public void add(Hotel hotel) {
        entityManager.persist(hotel);
    }

    @Transactional
    @Override
    public void add(List<Hotel> hotels) {
        for ( final Hotel hotel : hotels ) {
            entityManager.persist(hotel);
        }
    }
}
