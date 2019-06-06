package hotel.write.services.write;

import hotel.write.commands.CreateHotelCommand;
import hotel.write.cqrs.bus.Bus;
import hotel.write.domain.Hotel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HotelService {


    @Inject
    private Bus bus;

    public void addHotel(Hotel m) {
        System.out.println("bus.handleCommand new CreateHotelCommand");
        bus.handleCommand(new CreateHotelCommand(m));
    }

    public void addCodeName(String code,String name) {
        System.out.println("bus.handleCommand new CreateHotelCommand");
        bus.handleCommand(new CreateHotelCommand(new Hotel(code,name)));
    }
}
