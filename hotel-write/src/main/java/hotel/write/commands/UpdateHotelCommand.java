package hotel.write.commands;

import hotel.write.domain.Hotel;
import hotel.write.model.Command;
import hotel.write.model.HotelUpdateCommand;

/**
 * implements Command<Hotel> is bound to CreateHotelHandler .. AbstractCommandHandler
 */
public class UpdateHotelCommand implements Command<HotelUpdateCommand> {

	private HotelUpdateCommand hotel;

	public UpdateHotelCommand(HotelUpdateCommand m) {
		this.hotel = m;
	}

	public HotelUpdateCommand getHotel() {
		return hotel;
	}


}
