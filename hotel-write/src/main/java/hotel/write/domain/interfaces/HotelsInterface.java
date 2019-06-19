package hotel.write.domain.interfaces;



import hotel.write.commands.HotelCreatedCommand;
import hotel.write.commands.HotelDeleteCommand;
import hotel.write.commands.HotelSaveCommand;
import hotel.write.commands.HotelUpdateCommand;
import hotel.write.domain.Hotel;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface HotelsInterface {

	Optional<Hotel> findById(@NotNull Long id);



	Optional<Hotel> findByCode(String code);
	void save(Hotel hotel);
	void save(HotelSaveCommand hotelSaveCommand);
	void save(HotelCreatedCommand hotelCreatedCommand);
	void delete(HotelDeleteCommand hotel);
	void update(HotelUpdateCommand hotel);
	//Single<List<Hotel>> listAll(Map input);

	//Maybe<Hotel> reolveCode(String code);

	Hotel getByCode(String code);
	

}
