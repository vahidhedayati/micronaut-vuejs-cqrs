package hotel.write.domain.interfaces;


import hotel.write.event.commands.*;
import hotel.write.domain.Hotel;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface HotelsInterface {
	Optional<Hotel> findById(@NotNull Long id);
	Optional<Hotel> findByCode(String code);
	void save(Hotel hotel);
	<T extends CommandRoot> void  handleCommand(T  cmd);
	void handleCommand(HotelSaveCommand hotelSaveCommand);
	void handleCommand(HotelCreateCommand hotelCreatedCommand);
	void handleCommand(HotelDeleteCommand hotelDeleteCommand);
	void handleCommand(HotelUpdateCommand hotelUpdateCommand);

}
