package userbase.write.event.commandHandlers;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;
import userbase.write.domain.User;
import userbase.write.event.commands.UserUpdateCommand;
import userbase.write.event.events.EventRoot;
import userbase.write.event.kafka.EventPublisher;
import userbase.write.implementations.Users;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * AbstractCommandHandler is an internal listener for internal application events
 * It is triggered by KafkaListener which republishes dynamically received remote command object
 * This is then extended by relevant application commandHanders all within this folder structure.
 *
 */

@Singleton
public abstract class AbstractCommandHandler<E>  implements Users, ApplicationEventListener<E> {

    @PersistenceContext
    private EntityManager entityManager;

    private final EmbeddedServer embeddedServer;
    protected static final String topic = "userRead";
    private final EventPublisher eventPublisher;

    public AbstractCommandHandler(@CurrentSession EntityManager entityManager, EmbeddedServer embeddedServer,
                                  EventPublisher eventPublisher) {
        this.entityManager = entityManager;
        this.embeddedServer=embeddedServer;
        this.eventPublisher=eventPublisher;
    }
    @Transactional
    public void persistToDb(Object object) {
        entityManager.persist(object);
    }
    @Transactional
    public void removeFromDb(Object object) {
        entityManager.remove(object);
    }
    @Transactional
    public void updateDb(UserUpdateCommand cmd) {
        findById(cmd.getId()).ifPresent(user -> entityManager.createQuery("UPDATE User h  SET username = :username, password = :password, firstname=:firstname, surname=:surname where id = :id")
                .setParameter("username", cmd.getUsername())
                .setParameter("id", cmd.getId())
                .setParameter("password", cmd.getPassword())
                .setParameter("firstname", cmd.getFirstname())
                .setParameter("surname", cmd.getSurname())
                .executeUpdate()
        );
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
