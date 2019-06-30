package hotel.read.event.eventHandlers;


import hotel.read.domain.Hotel;
import hotel.read.domain.HotelRooms;
import hotel.read.event.events.HotelCreated;
import hotel.read.event.events.HotelRoomsCreated;
import hotel.read.implementation.ApplicationConfiguration;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class HotelCreatedEventHandler extends AbstractEventHandler<HotelCreated> {

    public HotelCreatedEventHandler(EntityManager entityManager, ApplicationConfiguration applicationConfiguration) {
        super(entityManager,applicationConfiguration);
    }

    @Override
    public void onApplicationEvent(HotelCreated cmd) {
        Hotel hotel = new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(),cmd.getUpdateUserId(),cmd.getLastUpdated(),cmd.getUpdateUserName().get());
        List<HotelRooms> hotelRooms = new ArrayList<>();
        if (!findByCode(hotel.getCode()).isPresent()) {
            merge(hotel);
            if (cmd.getHotelRooms() != null) {
                for (HotelRoomsCreated rmc : cmd.getHotelRooms()) {
                    HotelRooms hotelRooms1 = new HotelRooms(hotel, rmc.getRoomType(), rmc.getPrice(), rmc.getStockTotal());
                    hotelRooms.add(hotelRooms1);
                }
                hotel.setHotelRooms(hotelRooms);
            }
            persistToDb(hotel);
        }
    }
}
