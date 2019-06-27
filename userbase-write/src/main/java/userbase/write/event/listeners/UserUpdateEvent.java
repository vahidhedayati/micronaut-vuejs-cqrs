package userbase.write.event.listeners;


import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;
import userbase.write.domain.User;
import userbase.write.event.commands.UserSaveCommand;
import userbase.write.event.commands.UserUpdateCommand;
import userbase.write.event.events.UserSaved;
import userbase.write.event.events.UserUpdated;
import userbase.write.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class UserUpdateEvent implements ApplicationEventListener<UserUpdateCommand> {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService service;

    public UserUpdateEvent(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    @Override
    public void onApplicationEvent(UserUpdateCommand cmd) {
        UserUpdated cmd1 = new UserUpdated(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        service.publishEvent(cmd1);

        service.findById(cmd.getId()).ifPresent(user -> entityManager.createQuery("UPDATE User h  SET username = :username, password = :password, firstname=:firstname, surname=:surname where id = :id")
                .setParameter("username", cmd.getUsername())
                .setParameter("id", cmd.getId())
                .setParameter("password", cmd.getPassword())
                .setParameter("firstname", cmd.getFirstname())
                .setParameter("surname", cmd.getSurname())
                .executeUpdate()
        );
    }
}
