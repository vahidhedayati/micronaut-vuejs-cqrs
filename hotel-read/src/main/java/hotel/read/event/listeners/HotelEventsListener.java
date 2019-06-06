package hotel.read.event.listeners;

import hotel.read.domain.Hotel;
import hotel.read.domain.interfaces.Hotels;
import hotel.read.event.AbstractEvent;
import hotel.read.event.HotelCreatedEvent;
import hotel.read.services.read.QueryHotelViewDao;
import io.micronaut.context.event.ApplicationEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HotelEventsListener implements ApplicationEventListener<AbstractEvent<Hotel>> {

	protected static final Logger LOG = LoggerFactory.getLogger(HotelCreatedEvent.class);
	
	@Inject
	//private QueryHotelViewDao dao;
	private Hotels dao;


	@Override
	public void onApplicationEvent(AbstractEvent<Hotel> event) {
		LOG.debug("EVENT RECEIVED:" +  event);
		System.out.println(" READ ---------------------------------------------------------- EVENT RECEIVED: "+event);
		if (event instanceof HotelCreatedEvent) {
			LOG.debug("EVENT RECEIVED AT APPLICATION LISTENER");

			Hotel h = ((HotelCreatedEvent) event).getDtoFromEvent();
			System.out.println(" EVENT RECEIVED AT APPLICATION LISTENER ================"+h.getName());
			dao.save(h);
		}
	}
}
