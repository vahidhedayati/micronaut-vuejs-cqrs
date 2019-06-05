package commandservice.services.write;

import commandservice.commands.CreateHotelCommand;
import commandservice.cqrs.bus.Bus;
import commandservice.model.Hotel;
import commandservice.model.HotelSaveCommand;

import javax.inject.Inject;

public class HotelService {
	
	@Inject
	private Bus bus;
	
	public void addHotel(HotelSaveCommand m) {
		System.out.println("bus.handleCommand new CreateHotelCommand");
		bus.handleCommand(new CreateHotelCommand(m));
	}
}
