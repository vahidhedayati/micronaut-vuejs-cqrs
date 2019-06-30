package hotel.read.event.eventHandlers;


import hotel.read.event.events.HotelUpdated;
import hotel.read.implementation.ApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HotelUpdatedEventHandler extends AbstractEventHandler<HotelUpdated> {

    public HotelUpdatedEventHandler(EntityManager entityManager, ApplicationConfiguration applicationConfiguration) {
        super(entityManager,applicationConfiguration);
    }

    @Override
    public void onApplicationEvent(HotelUpdated cmd) {
        updateDb(cmd);
    }
}
