package commandservice.services.write;

import commandservice.model.Hotel;
import commandservice.model.HotelSaveCommand;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class HotelDaoWriteList implements Dao<HotelSaveCommand> {

	private List<HotelSaveCommand> hotels = new ArrayList<>();

	public void save(HotelSaveCommand hotel) {

		System.out.println("Adding hotel to arrayList");
		hotels.add(hotel);
	}
}
