package commandservice.commands;

import commandservice.model.Command;
import commandservice.model.Hotel;
import commandservice.model.HotelSaveCommand;

/**
 * implements Command<Hotel> is bound to CreateHotelHandler .. AbstractCommandHandler
 */
public class CreateHotelCommand implements Command<HotelSaveCommand> {
	
	private HotelSaveCommand hotel;

	public CreateHotelCommand(HotelSaveCommand m) {
		this.hotel = m;
	}

	public HotelSaveCommand getHotel() {
		return hotel;
	}


}
