package hotel.read.domain.interfaces;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.commands.HotelCreatedCommand;
import hotel.read.commands.HotelDeletedCommand;
import hotel.read.commands.HotelSavedCommand;
import hotel.read.commands.HotelUpdatedCommand;
import hotel.read.domain.Hotel;
import hotel.read.implementation.SortingAndOrderArguments;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface HotelsInterface {

	Optional<Hotel> findById(@NotNull Long id);

	Optional<HotelModel> findAll(@NotNull SortingAndOrderArguments args);


	Optional<Hotel> findByCode(String code);
	void save(Hotel hotel);
	void save(HotelSavedCommand hotelSaveCommand);
	void save(HotelCreatedCommand hotelCreatedCommand);
	void delete(HotelDeletedCommand hotel);
	void update(HotelUpdatedCommand hotel);

	Hotel getByCode(String code);
	

}
