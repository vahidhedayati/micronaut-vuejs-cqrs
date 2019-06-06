package hotel.write.services.write;

import hotel.write.domain.Hotel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface Hotels {

	void deleteById(@NotNull Long id);

	int update(@NotNull Long id, @NotBlank String name, String code, @NotBlank String phone, @NotBlank String email);

	void addHotel(Hotel hotel);

	Hotel save(@NotBlank String code, @NotBlank String name);
	
	void add(List<Hotel> hotel);
}
