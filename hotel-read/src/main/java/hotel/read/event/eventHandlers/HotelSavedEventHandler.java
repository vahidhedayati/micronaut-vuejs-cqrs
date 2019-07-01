package hotel.read.event.eventHandlers;


import hotel.read.domain.Hotel;
import hotel.read.event.events.HotelSaved;
import hotel.read.implementation.ApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HotelSavedEventHandler extends AbstractEventHandler<HotelSaved> {

    public HotelSavedEventHandler(EntityManager entityManager, ApplicationConfiguration applicationConfiguration) {
        super(entityManager,applicationConfiguration);
    }

    @Override
    public void onApplicationEvent(HotelSaved cmd) {
        Hotel h = new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(), cmd.getUpdateUserId(), cmd.getUpdateUserName().get());
        save(h);
    }
}
