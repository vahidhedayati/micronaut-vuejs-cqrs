package userbase.read.event.eventHandlers;


import userbase.read.event.events.UserUpdated;
import userbase.read.implementations.MyApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class UserUpdatedEventHandler extends AbstractEventHandler<UserUpdated> {

    public UserUpdatedEventHandler(EntityManager entityManager,
                                 MyApplicationConfiguration myApplicationConfiguration) {
        super(entityManager,myApplicationConfiguration);
    }

    @Override
    public void onApplicationEvent(UserUpdated cmd) {
       updateDb(cmd);
    }
}
