package hotel.read.event.listeners;

import hotel.read.domain.interfaces.Hotels;
import hotel.read.event.HotelCreatedEvent;
import hotel.read.services.read.QueryHotelViewDao;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Primary;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@KafkaListener
@Primary
public class KafkaCustomEventListener {
	
	protected static final Logger LOG = LoggerFactory.getLogger(KafkaCustomEventListener.class);
	
	@Inject
	private Hotels dao;
	//private QueryHotelViewDao dao;
	
	@Topic("hotelCreated")
	public void consume( @KafkaKey String movieId, @Body HotelCreatedEvent hotelCreatedEvent) {
		LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER");
		System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER");
		dao.save(hotelCreatedEvent.getDtoFromEvent());
	}
}
