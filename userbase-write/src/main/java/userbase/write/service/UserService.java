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


    /**
     * This publishes to hotelRead Topic - picked up by hotelRead microservice
     * @param cmd
     */
    public void publishEvent(EventRoot cmd) {
        eventPublisher.publish(embeddedServer,topic,cmd);
    }

}
