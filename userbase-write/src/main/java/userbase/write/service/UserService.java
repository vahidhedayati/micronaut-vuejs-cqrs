package userbase.write.service;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.spring.tx.annotation.Transactional;
import userbase.write.commands.Command;
import userbase.write.commands.UserDeleteCommand;
import userbase.write.commands.UserSaveCommand;
import userbase.write.commands.UserUpdateCommand;
import userbase.write.domain.User;
import userbase.write.implementations.MyApplicationConfiguration;
import userbase.write.implementations.Users;
import userbase.write.kafka.EventPublisher;
import userbase.write.models.SortingAndOrderArguments;
import userbase.write.models.UserModel;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
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



    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        findById(id).ifPresent(user -> entityManager.remove(user));
    }

    @Transactional
    @Override
    public int update(@NotNull Long id, @NotBlank String username, @NotBlank String password, @NotBlank String firstname, @NotBlank String surname) {
        return entityManager.createQuery("UPDATE User h  SET username = :username, password = :password, firstname=:firstname, surname=:surname where id = :id")
                .setParameter("username", username)
                .setParameter("id", id)
                .setParameter("password", password)
                .setParameter("firstname", firstname)
                .setParameter("surname", surname)
                .executeUpdate();
    }



    @Transactional
    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Transactional
    @Override
    public User save(@NotBlank String username, @NotBlank String password, @NotBlank String firstname, @NotBlank String surname) {
        User user = new User(username,password,firstname,surname);
        entityManager.persist(user);
        return user;
    }

    @Transactional
    @Override
    public void save(UserSaveCommand cmd) {

        cmd.setEventType("UserSavedCommand");
        publishEvent(cmd);

        System.out.println("Doing user save "+cmd.getUsername());
        User user = new User(cmd.getUsername(),cmd.getPassword(),cmd.getFirstname(),cmd.getSurname(),cmd.getLastUpdated());
        entityManager.persist(user);
    }

    @Transactional
    @Override
    public void delete(UserDeleteCommand cmd) {
        cmd.setEventType("UserDeletedCommand");
        publishEvent(cmd);

        System.out.println("Doing User delete "+cmd.getId());
        findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }

    @Transactional
    @Override
    public void update(UserUpdateCommand cmd) {
        cmd.setEventType("UserUpdatedCommand");
        publishEvent(cmd);

        System.out.println("Doing user update "+cmd.getUsername());
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
    public void publishEvent(Command cmd) {
        eventPublisher.publish(embeddedServer,topic,cmd);
    }

    @Transactional
    @Override
    public void add(List<User> users) {
        for ( final User user : users ) {
            System.out.println(" Adding user: "+user.getFirstname());
            entityManager.persist(user);
        }
    }
}
