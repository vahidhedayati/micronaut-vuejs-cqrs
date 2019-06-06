package hotel.write.init;

import hotel.write.domain.Hotel;
import hotel.write.services.write.HotelDaoWriteDb;
import hotel.write.services.write.HotelService;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.spring.tx.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class DataLoader  implements ApplicationEventListener<ServerStartupEvent> {

	@Inject
	private HotelDaoWriteDb hotelDb;

	@Inject
	private HotelService hotelService;
	 
	@Transactional
	@Override
	public void onApplicationEvent(ServerStartupEvent event) {
		//if (!hotelDb.findByCode("HILL").isPresent()) {
		List<Hotel> hotels = DemoHotelsFactory.defaultHotels();

		//This adds directly to db 
		// hotelDb.add(hotels);

		for ( final Hotel hotel : hotels ) {
			//System.out.println(" working on "+hotel.getCode());
			//hotelDb.add(hotel);
			//This submits hotel to be added as well as an event to any listeners of kafka hotel-add stream
			hotelService.addHotel(hotel);
            //Hotel hotel1 =  DemoHotelsFactory.addHotel(hotel);
           // hotelDb.add(hotel1);

		}
			//hotelDb.add(hotels);
        }

	//}
}
