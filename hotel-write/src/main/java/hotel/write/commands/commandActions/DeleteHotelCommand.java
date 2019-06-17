package hotel.write.commands.commandActions;

import hotel.write.commands.HotelDeleteCommand;
import hotel.write.domain.Hotel;
import hotel.write.model.Command;

/**
 * implements Command<Hotel> is bound to CreateHotelHandler .. AbstractCommandHandler
 */
public class DeleteHotelCommand implements Command<Hotel> {

	private HotelDeleteCommand hotel;

	public DeleteHotelCommand(HotelDeleteCommand m) {
		this.hotel = m;
	}

	public HotelDeleteCommand getHotel() {
		return hotel;
	}

	public Hotel getActualHotel() {
		return hotel.getHotel();
	}


}
