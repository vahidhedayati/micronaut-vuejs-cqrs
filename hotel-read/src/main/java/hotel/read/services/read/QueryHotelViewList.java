package hotel.read.services.read;

import hotel.read.domain.Hotel;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class QueryHotelViewList implements QueryHotelViewDao {

	List<Hotel> hotels = new ArrayList<>();

	public List<Hotel> getHotels() {
		return hotels;
	}

	public void save(Hotel m) {
		hotels.add(m);
	}

}
