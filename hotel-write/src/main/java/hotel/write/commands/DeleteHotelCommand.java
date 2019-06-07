package hotel.write.commands;

import hotel.write.domain.Hotel;
import hotel.write.model.Command;
import hotel.write.model.HotelDeleteCommand;

/**
 * implements Command<Hotel> is bound to CreateHotelHandler .. AbstractCommandHandler
 */
public class DeleteHotelCommand implements Command<HotelDeleteCommand> {

	private HotelDeleteCommand hotel;

	public DeleteHotelCommand(HotelDeleteCommand m) {
		this.hotel = m;
	}

	public HotelDeleteCommand getHotel() {
		return hotel;
	}


}
