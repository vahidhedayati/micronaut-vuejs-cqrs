package userbase.write.event.commandHandlers;


import io.micronaut.runtime.server.EmbeddedServer;
import userbase.write.event.commands.UserUpdateCommand;
import userbase.write.event.events.UserUpdated;
import userbase.write.event.kafka.EventPublisher;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class UserUpdateCommandHandler extends AbstractCommandHandler<UserUpdateCommand> {


    public UserUpdateCommandHandler(EntityManager entityManager, EmbeddedServer embeddedServer,
                                  EventPublisher eventPublisher) {
        super(entityManager,embeddedServer,eventPublisher);
    }


    @Override
    public void onApplicationEvent(UserUpdateCommand cmd) {
        UserUpdated cmd1 = new UserUpdated(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);
        updateDb(cmd);
    }
}
