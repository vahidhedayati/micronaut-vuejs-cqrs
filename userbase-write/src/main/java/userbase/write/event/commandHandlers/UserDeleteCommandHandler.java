package userbase.write.event.commandHandlers;


import io.micronaut.runtime.server.EmbeddedServer;
import userbase.write.event.commands.UserDeleteCommand;
import userbase.write.event.events.UserDeleted;
import userbase.write.event.kafka.EventPublisher;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class UserDeleteCommandHandler extends AbstractCommandHandler<UserDeleteCommand> {


    public UserDeleteCommandHandler(EntityManager entityManager, EmbeddedServer embeddedServer,
                                  EventPublisher eventPublisher) {
        super(entityManager,embeddedServer,eventPublisher);
    }

    @Override
    public void onApplicationEvent(UserDeleteCommand cmd) {
        UserDeleted cmd1 = new UserDeleted(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        findById(cmd.getId()).ifPresent(hotel ->
                removeFromDb(hotel)
        );

    }
}
