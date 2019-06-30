package hotel.write.event.commandHandlers;

import hotel.write.clients.UserReadClient;
import hotel.write.domain.Hotel;
import hotel.write.domain.HotelRooms;
import hotel.write.event.commands.HotelCreateCommand;
import hotel.write.event.commands.HotelRoomsCreateCommand;
import hotel.write.event.events.HotelCreated;
import hotel.write.event.kafka.EventPublisher;
import hotel.write.implementations.ApplicationConfiguration;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class HotelCreateCommandHandler extends AbstractCommandHandler<HotelCreateCommand> {

    public HotelCreateCommandHandler(EntityManager entityManager, ApplicationConfiguration applicationConfiguration,
                                     EventPublisher eventPublisher, EmbeddedServer embeddedServer, UserReadClient userReadClient) {
        super(entityManager, applicationConfiguration, eventPublisher, embeddedServer, userReadClient);
    }

    @Override
    public void onApplicationEvent(HotelCreateCommand cmd) {
        HotelCreated cmd1 = new HotelCreated(cmd);
        cmd1.setUpdateUserName(userReadClient.findById(cmd.getUpdateUserId()).map(u -> u.getUsername()));
        cmd1.setEventType(cmd1.getClass().getSimpleName());
        publishEvent(cmd1);

        Hotel hotel = new Hotel(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail(), cmd.getUpdateUserId(), cmd.getLastUpdated());
        List<HotelRooms> hotelRooms = new ArrayList<>();
        if (!findByCode(hotel.getCode()).isPresent()) {
            merge(hotel);
            for (HotelRoomsCreateCommand rmc : cmd.getHotelRooms()) {
                HotelRooms hotelRooms1 = new HotelRooms(hotel, rmc.getRoomType(), rmc.getPrice(), rmc.getStockTotal());
                hotelRooms.add(hotelRooms1);
            }
            hotel.setHotelRooms(hotelRooms);
            persistToDb(hotel);

        }
    }
}
