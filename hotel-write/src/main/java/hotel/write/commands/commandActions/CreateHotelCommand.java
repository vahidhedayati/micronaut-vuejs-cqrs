package hotel.write.commands.commandActions;

import hotel.write.commands.HotelCreatedCommand;
import hotel.write.domain.Hotel;
import hotel.write.model.Command;

/**
 * implements Command<Hotel> is bound to CreateHotelHandler .. AbstractCommandHandler
 */
public class CreateHotelCommand implements Command<HotelCreatedCommand> {
	
	private HotelCreatedCommand hotelCreatedCommand;

	public CreateHotelCommand(HotelCreatedCommand m) {
		this.hotelCreatedCommand = m;
	}

	public HotelCreatedCommand getHotelCreatedCommand() {
		return hotelCreatedCommand;
	}


}
