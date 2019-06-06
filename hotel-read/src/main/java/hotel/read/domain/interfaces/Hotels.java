package hotel.read.domain.interfaces;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.domain.Hotel;
import hotel.read.implementation.SortingAndOrderArguments;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface Hotels {

	Optional<Hotel> findById(@NotNull Long id);

	Optional<HotelModel> findAll(@NotNull SortingAndOrderArguments args);


	Optional<Hotel> findByCode(String code);
	void save(Hotel hotel);
	//Single<List<Hotel>> listAll(Map input);

	//Maybe<Hotel> reolveCode(String code);

	Hotel getByCode(String code);
	

}
