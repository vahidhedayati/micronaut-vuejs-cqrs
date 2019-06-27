package userbase.write.event.listeners;


import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;
import userbase.write.event.commands.UserDeleteCommand;
import userbase.write.event.events.UserDeleted;
import userbase.write.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class UserDeleteEvent implements ApplicationEventListener<UserDeleteCommand> {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService service;

    public UserDeleteEvent(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    @Override
    public void onApplicationEvent(UserDeleteCommand cmd) {
        UserDeleted cmd1 = new UserDeleted(cmd);
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        service.publishEvent(cmd1);
        service.findById(cmd.getId()).ifPresent(hotel ->
                entityManager.remove(hotel)
        );
    }
}
