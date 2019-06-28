package userbase.write.event.commandHandlers;


import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.server.EmbeddedServer;
import userbase.write.event.commands.UserUpdateCommand;
import userbase.write.event.events.UserUpdated;
import userbase.write.event.kafka.EventPublisher;
import userbase.write.implementations.MyApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class UserUpdateCommandHandler extends AbstractCommandHandler<UserUpdateCommand> {


    public UserUpdateCommandHandler(@CurrentSession EntityManager entityManager, EmbeddedServer embeddedServer,
                                  MyApplicationConfiguration myApplicationConfiguration, EventPublisher eventPublisher) {
        super(entityManager,embeddedServer,myApplicationConfiguration,eventPublisher);
    }


    @Override
    public void onApplicationEvent(UserUpdateCommand cmd) {
        UserUpdated cmd1 = new UserUpdated(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        findById(cmd.getId()).ifPresent(user -> getEntityManager().createQuery("UPDATE User h  SET username = :username, password = :password, firstname=:firstname, surname=:surname where id = :id")
                .setParameter("username", cmd.getUsername())
                .setParameter("id", cmd.getId())
                .setParameter("password", cmd.getPassword())
                .setParameter("firstname", cmd.getFirstname())
                .setParameter("surname", cmd.getSurname())
                .executeUpdate()
        );
    }
}
