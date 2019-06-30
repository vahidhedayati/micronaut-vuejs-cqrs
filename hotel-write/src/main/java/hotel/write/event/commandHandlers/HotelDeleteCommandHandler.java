package hotel.write.event.commandHandlers;

import hotel.write.clients.UserReadClient;
import hotel.write.event.commands.HotelDeleteCommand;
import hotel.write.event.events.HotelDeleted;
import hotel.write.event.kafka.EventPublisher;
import hotel.write.implementations.ApplicationConfiguration;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HotelDeleteCommandHandler extends AbstractCommandHandler<HotelDeleteCommand> {


    public HotelDeleteCommandHandler(EntityManager entityManager, ApplicationConfiguration applicationConfiguration,
                                     EventPublisher eventPublisher, EmbeddedServer embeddedServer, UserReadClient userReadClient) {
        super(entityManager,applicationConfiguration,eventPublisher,embeddedServer,userReadClient);
    }
    @Override
    public void onApplicationEvent(HotelDeleteCommand cmd) {
        HotelDeleted cmd1 = new HotelDeleted(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);
            findById(cmd.getId()).ifPresent(hotel -> removeFromDb(hotel));

    }
}
