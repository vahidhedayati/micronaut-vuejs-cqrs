package userbase.read.event.eventHandlers;


import userbase.read.domain.User;
import userbase.read.event.events.UserSaved;
import userbase.read.implementations.MyApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class UserSavedEventHandler extends AbstractEventHandler<UserSaved> {

    public UserSavedEventHandler(EntityManager entityManager,
                                 MyApplicationConfiguration myApplicationConfiguration) {
        super(entityManager,myApplicationConfiguration);
    }

    @Override
    public void onApplicationEvent(UserSaved cmd) {
        User user = new User(cmd.getUsername(), cmd.getPassword(), cmd.getFirstname(), cmd.getSurname(), cmd.getLastUpdated());
        persistToDb(user);
    }
}
