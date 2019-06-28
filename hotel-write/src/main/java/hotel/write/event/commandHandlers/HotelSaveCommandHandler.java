package hotel.write.event.commandHandlers;

import hotel.write.clients.UserReadClient;
import hotel.write.domain.Hotel;
import hotel.write.event.commands.HotelSaveCommand;
import hotel.write.event.events.HotelSaved;
import hotel.write.event.kafka.EventPublisher;
import hotel.write.implementations.ApplicationConfiguration;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HotelSaveCommandHandler extends AbstractCommandHandler<HotelSaveCommand> {
    public HotelSaveCommandHandler(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration,
                                     EventPublisher eventPublisher, EmbeddedServer embeddedServer, UserReadClient userReadClient) {
        super(entityManager,applicationConfiguration,eventPublisher,embeddedServer,userReadClient);
    }

    @Override
    public void onApplicationEvent(HotelSaveCommand cmd) {
        HotelSaved cmd1 = new HotelSaved(cmd);
        cmd1.setUpdateUserName(getUserReadClient().findById(cmd.getUpdateUserId()).map(u->u.getUsername()));
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        save(new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail()));
    }
}
