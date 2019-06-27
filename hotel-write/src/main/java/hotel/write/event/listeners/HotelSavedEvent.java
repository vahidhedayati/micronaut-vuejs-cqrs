package hotel.write.event.listeners;

import hotel.write.clients.UserReadClient;
import hotel.write.domain.Hotel;
import hotel.write.domain.HotelRooms;
import hotel.write.event.CommandBus;
import hotel.write.event.commands.HotelCreateCommand;
import hotel.write.event.commands.HotelRoomsCreateCommand;
import hotel.write.event.commands.HotelSaveCommand;
import hotel.write.event.events.HotelCreated;
import hotel.write.event.events.HotelSaved;
import hotel.write.services.write.HotelService;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.event.ApplicationEventListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class HotelSavedEvent implements ApplicationEventListener<HotelSaveCommand> {
    @PersistenceContext
    private EntityManager entityManager;
    private final UserReadClient userReadClient;
    @Inject
    private HotelService service;

    public HotelSavedEvent(@CurrentSession EntityManager entityManager, UserReadClient userReadClient) {
        this.entityManager = entityManager;
        this.userReadClient=userReadClient;
    }

    @Override
    public void onApplicationEvent(HotelSaveCommand cmd) {

        HotelSaved cmd1 = new HotelSaved(cmd);
        cmd1.setUpdateUserName(userReadClient.findById(cmd.getUpdateUserId()).map(u->u.getUsername()));
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        service.publishEvent(cmd1);

        service.save(new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail()));
    }
}
