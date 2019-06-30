package userbase.write.event.commandHandlers;


import io.micronaut.runtime.server.EmbeddedServer;
import userbase.write.domain.User;
import userbase.write.event.commands.UserSaveCommand;
import userbase.write.event.events.UserSaved;
import userbase.write.event.kafka.EventPublisher;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class UserSaveCommandHandler extends AbstractCommandHandler<UserSaveCommand> {

    public UserSaveCommandHandler(EntityManager entityManager, EmbeddedServer embeddedServer,
                                    EventPublisher eventPublisher) {
        super(entityManager,embeddedServer,eventPublisher);
    }

    @Override
    public void onApplicationEvent(UserSaveCommand cmd) {
        UserSaved cmd1 = new UserSaved(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
       publishEvent(cmd1);

        User user = new User(cmd.getUsername(),cmd.getPassword(),cmd.getFirstname(),cmd.getSurname(),cmd.getLastUpdated());

            persistToDb(user);

    }
}
