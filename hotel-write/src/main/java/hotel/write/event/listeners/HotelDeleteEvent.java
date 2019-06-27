package hotel.write.event.listeners;

import hotel.write.clients.UserReadClient;
import hotel.write.domain.Hotel;
import hotel.write.event.commands.HotelDeleteCommand;
import hotel.write.event.commands.HotelSaveCommand;
import hotel.write.event.events.HotelDeleted;
import hotel.write.event.events.HotelSaved;
import hotel.write.services.write.HotelService;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class HotelDeleteEvent implements ApplicationEventListener<HotelDeleteCommand> {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private HotelService service;

    public HotelDeleteEvent(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void onApplicationEvent(HotelDeleteCommand cmd) {
        HotelDeleted cmd1 = new HotelDeleted(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        service.publishEvent(cmd1);
        service.findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }
}
