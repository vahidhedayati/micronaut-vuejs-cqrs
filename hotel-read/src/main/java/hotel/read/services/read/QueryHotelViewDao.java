package hotel.read.services.read;

import hotel.read.domain.Hotel;

import java.util.List;

public interface QueryHotelViewDao {

	List<Hotel> getHotels();
	
	void save(Hotel hotel);

}
