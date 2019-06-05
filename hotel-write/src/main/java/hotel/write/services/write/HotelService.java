package hotel.write.services.write;

import hotel.write.commands.CreateHotelCommand;
import hotel.write.cqrs.bus.Bus;
import hotel.write.model.HotelSaveCommand;

import javax.inject.Inject;

public class HotelService {
	
	@Inject
	private Bus bus;
	
	public void addHotel(HotelSaveCommand m) {
		System.out.println("bus.handleCommand new CreateHotelCommand");
		bus.handleCommand(new CreateHotelCommand(m));
	}
}
