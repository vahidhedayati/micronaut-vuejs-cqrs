package hotel.read.services.read;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.domain.Hotel;
import hotel.read.implementation.SortingAndOrderArguments;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface QueryHotelViewDao {

	Optional<HotelModel> findAll(@NotNull SortingAndOrderArguments args);
	Optional<Hotel> findById(@NotNull Long id);
	Optional<Hotel> findByCode(String code);

	//List<Hotel> getHotels();
	
	void save(Hotel hotel);

}
