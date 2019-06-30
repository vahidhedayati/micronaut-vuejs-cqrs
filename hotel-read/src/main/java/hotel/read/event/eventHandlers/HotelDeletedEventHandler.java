package hotel.read.event.eventHandlers;


import hotel.read.event.events.HotelDeleted;
import hotel.read.implementation.ApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HotelDeletedEventHandler extends AbstractEventHandler<HotelDeleted> {

    public HotelDeletedEventHandler(EntityManager entityManager, ApplicationConfiguration applicationConfiguration) {
        super(entityManager,applicationConfiguration);
    }

    @Override
    public void onApplicationEvent(HotelDeleted cmd) {
        findById(cmd.getId()).ifPresent(hotel -> removeFromDb(hotel));
    }
}
