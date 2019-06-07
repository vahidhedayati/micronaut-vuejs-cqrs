package hotel.write.services.write;

import hotel.write.domain.Hotel;
import hotel.write.implementations.ApplicationConfiguration;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

//implements Hotels,

@Singleton
public class HotelDaoWriteDb  implements Dao<Hotel>  {


    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;



    public HotelDaoWriteDb(@CurrentSession EntityManager entityManager,
                           ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }





    @Transactional
    public Hotel addCodeName(@NotBlank String code, @NotBlank String name) {
        Hotel hotel = new Hotel(code,name);
        entityManager.persist(hotel);
        return hotel;
    }



    @Transactional
    public void deleteById(@NotNull Long id) {
       // findById(id).ifPresent(hotel -> entityManager.remove(hotel));
    }


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
    public void add(List<Hotel> hotels) {
        for ( final Hotel hotel : hotels ) {
            entityManager.persist(hotel);
        }
    }

    @Override
    @Transactional
    public void save(Hotel hotel) {
        entityManager.persist(hotel);
        // System.out.println("bus.handleCommand new CreateHotelCommand");
    }
}
