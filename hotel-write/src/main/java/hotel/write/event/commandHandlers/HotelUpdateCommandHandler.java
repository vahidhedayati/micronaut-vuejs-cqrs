package hotel.write.event.commandHandlers;

import hotel.write.clients.UserReadClient;
import hotel.write.event.commands.HotelUpdateCommand;
import hotel.write.event.events.HotelUpdated;
import hotel.write.event.kafka.EventPublisher;
import hotel.write.implementations.ApplicationConfiguration;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HotelUpdateCommandHandler  extends AbstractCommandHandler<HotelUpdateCommand> {

    public HotelUpdateCommandHandler(@CurrentSession EntityManager entityManager, ApplicationConfiguration applicationConfiguration,
                                   EventPublisher eventPublisher, EmbeddedServer embeddedServer, UserReadClient userReadClient) {
        super(entityManager,applicationConfiguration,eventPublisher,embeddedServer,userReadClient);
    }

    @Override
    public void onApplicationEvent(HotelUpdateCommand cmd) {
        HotelUpdated cmd1 = new HotelUpdated(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        findById(cmd.getId()).ifPresent(hotel -> getEntityManager().createQuery("UPDATE Hotel h  SET name = :name, code = :code, email = :email, phone = :phone  where id = :id")
                .setParameter("name", cmd.getName())
                .setParameter("id", cmd.getId())
                .setParameter("code", cmd.getCode())
                .setParameter("phone", cmd.getPhone())
                .setParameter("email", cmd.getEmail())
                .executeUpdate()
        );
    }
}
