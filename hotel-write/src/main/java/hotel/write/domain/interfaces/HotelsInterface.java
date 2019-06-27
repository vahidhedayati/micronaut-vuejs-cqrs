package hotel.write.domain.interfaces;


import hotel.write.domain.Hotel;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface HotelsInterface {
	Optional<Hotel> findById(@NotNull Long id);
	Optional<Hotel> findByCode(String code);
	void save(Hotel hotel);
}
