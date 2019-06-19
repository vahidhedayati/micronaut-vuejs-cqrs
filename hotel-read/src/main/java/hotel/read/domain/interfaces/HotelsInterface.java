package hotel.read.domain.interfaces;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.commands.HotelCreatedCommand;
import hotel.read.commands.HotelDeleteCommand;
import hotel.read.commands.HotelSaveCommand;
import hotel.read.commands.HotelUpdateCommand;
import hotel.read.domain.Hotel;
import hotel.read.implementation.SortingAndOrderArguments;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface HotelsInterface {

	Optional<Hotel> findById(@NotNull Long id);

	Optional<HotelModel> findAll(@NotNull SortingAndOrderArguments args);


	Optional<Hotel> findByCode(String code);
	void save(Hotel hotel);
	void save(HotelSaveCommand hotelSaveCommand);
	void save(HotelCreatedCommand hotelCreatedCommand);
	void delete(HotelDeleteCommand hotel);
	void update(HotelUpdateCommand hotel);

	Hotel getByCode(String code);
	

}
