package hotel.write.event.listeners;

import hotel.write.clients.UserReadClient;
import hotel.write.event.commands.HotelDeleteCommand;
import hotel.write.event.commands.HotelUpdateCommand;
import hotel.write.event.events.HotelDeleted;
import hotel.write.event.events.HotelUpdated;
import hotel.write.services.write.HotelService;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class HotelUpdateEvent implements ApplicationEventListener<HotelUpdateCommand> {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private HotelService service;

    public HotelUpdateEvent(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    @Override
    public void onApplicationEvent(HotelUpdateCommand cmd) {
        HotelUpdated cmd1 = new HotelUpdated(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        service.publishEvent(cmd1);

        service.findById(cmd.getId()).ifPresent(hotel -> entityManager.createQuery("UPDATE Hotel h  SET name = :name, code = :code, email = :email, phone = :phone  where id = :id")
                .setParameter("name", cmd.getName())
                .setParameter("id", cmd.getId())
                .setParameter("code", cmd.getCode())
                .setParameter("phone", cmd.getPhone())
                .setParameter("email", cmd.getEmail())
                .executeUpdate()
        );
    }
}
