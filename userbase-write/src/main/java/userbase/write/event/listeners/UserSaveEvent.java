package userbase.write.event.listeners;


import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;
import userbase.write.domain.User;
import userbase.write.event.commands.UserDeleteCommand;
import userbase.write.event.commands.UserSaveCommand;
import userbase.write.event.events.UserDeleted;
import userbase.write.event.events.UserSaved;
import userbase.write.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class UserSaveEvent implements ApplicationEventListener<UserSaveCommand> {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService service;

    public UserSaveEvent(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    @Override
    public void onApplicationEvent(UserSaveCommand cmd) {
        UserSaved cmd1 = new UserSaved(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
       service.publishEvent(cmd1);

        User user = new User(cmd.getUsername(),cmd.getPassword(),cmd.getFirstname(),cmd.getSurname(),cmd.getLastUpdated());
        entityManager.persist(user);
    }
}
