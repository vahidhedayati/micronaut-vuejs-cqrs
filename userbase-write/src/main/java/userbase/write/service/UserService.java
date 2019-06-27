package userbase.write.service;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;
import userbase.write.domain.User;
import userbase.write.event.commands.CommandRoot;
import userbase.write.event.commands.UserDeleteCommand;
import userbase.write.event.commands.UserSaveCommand;
import userbase.write.event.commands.UserUpdateCommand;
import userbase.write.event.events.EventRoot;
import userbase.write.event.events.UserDeleted;
import userbase.write.event.events.UserSaved;
import userbase.write.event.events.UserUpdated;
import userbase.write.event.kafka.EventPublisher;
import userbase.write.implementations.MyApplicationConfiguration;
import userbase.write.implementations.Users;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public class UserService implements Users {

    @PersistenceContext
    private EntityManager entityManager;
    private final MyApplicationConfiguration myApplicationConfiguration;

    private final EmbeddedServer embeddedServer;
    protected static final String topic = "userRead";
    private final EventPublisher eventPublisher;

    public UserService(@CurrentSession EntityManager entityManager,EmbeddedServer embeddedServer,
                       MyApplicationConfiguration myApplicationConfiguration,EventPublisher eventPublisher) {
        this.entityManager = entityManager;
        this.embeddedServer=embeddedServer;
        this.myApplicationConfiguration = myApplicationConfiguration;
        this.eventPublisher=eventPublisher;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Transactional
    @Override
    public Optional<User> findByUsername(String username) {
        return entityManager
                .createQuery("from User h where h.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional
    public <T extends CommandRoot> void  handleCommand(T  cmd) {
        if (cmd instanceof UserSaveCommand) {
            handleCommand((UserSaveCommand) cmd);
        } else if (cmd instanceof UserDeleteCommand) {
            handleCommand((UserDeleteCommand) cmd);
        } else if (cmd instanceof UserUpdateCommand) {
            handleCommand((UserUpdateCommand) cmd);
        }
    }

    @Transactional
    @Override
    public void handleCommand(UserSaveCommand cmd) {

        UserSaved cmd1 = new UserSaved(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        User user = new User(cmd.getUsername(),cmd.getPassword(),cmd.getFirstname(),cmd.getSurname(),cmd.getLastUpdated());
        entityManager.persist(user);
    }

    @Transactional
    @Override
    public void handleCommand(UserDeleteCommand cmd) {

        UserDeleted cmd1 = new UserDeleted(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }

    @Transactional
    @Override
    public void handleCommand(UserUpdateCommand cmd) {

        UserUpdated cmd1 = new UserUpdated(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        findById(cmd.getId()).ifPresent(user -> entityManager.createQuery("UPDATE User h  SET username = :username, password = :password, firstname=:firstname, surname=:surname where id = :id")
                .setParameter("username", cmd.getUsername())
                .setParameter("id", cmd.getId())
                .setParameter("password", cmd.getPassword())
                .setParameter("firstname", cmd.getFirstname())
                .setParameter("surname", cmd.getSurname())
                .executeUpdate()
        );
    }

    /**
     * This publishes to hotelRead Topic - picked up by hotelRead microservice
     * @param cmd
     */
    public void publishEvent(EventRoot cmd) {
        eventPublisher.publish(embeddedServer,topic,cmd);
    }

}
