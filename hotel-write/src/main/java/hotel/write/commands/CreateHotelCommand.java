package hotel.write.commands;

import hotel.write.domain.Hotel;
import hotel.write.model.Command;

/**
 * implements Command<Hotel> is bound to CreateHotelHandler .. AbstractCommandHandler
 */
public class CreateHotelCommand implements Command<Hotel> {
	
	private Hotel hotel;

	public CreateHotelCommand(Hotel m) {
		this.hotel = m;
	}

	public Hotel getHotel() {
		return hotel;
	}


}
