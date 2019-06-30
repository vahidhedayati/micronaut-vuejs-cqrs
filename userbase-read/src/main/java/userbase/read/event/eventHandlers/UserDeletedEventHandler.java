package userbase.read.event.eventHandlers;


import userbase.read.event.events.UserDeleted;
import userbase.read.implementations.MyApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class UserDeletedEventHandler extends AbstractEventHandler<UserDeleted> {

    public UserDeletedEventHandler(EntityManager entityManager,
                                MyApplicationConfiguration myApplicationConfiguration) {
        super(entityManager,myApplicationConfiguration);
    }

    @Override
    public void onApplicationEvent(UserDeleted cmd) {
       findById(cmd.getId()).ifPresent(hotel -> removeFromDb(hotel));
    }
}
