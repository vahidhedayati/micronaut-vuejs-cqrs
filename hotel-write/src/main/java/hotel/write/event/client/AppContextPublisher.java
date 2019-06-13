package hotel.write.event.client;

import hotel.write.domain.Hotel;
import hotel.write.event.AbstractEvent;
import hotel.write.model.HotelSaveCommand;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.event.ApplicationEventPublisher;

import javax.inject.Inject;
@Primary
public class AppContextPublisher implements EventPublisher<Hotel> {
	
	@Inject
	ApplicationEventPublisher publisher;

	@Override
	public void publish(AbstractEvent<Hotel> event) {
		System.out.println(" publisher publishing: " +event);
		publisher.publishEvent(event);
	}

	public void publishEdit(AbstractEvent<Hotel> event) {
		System.out.println(" publisher publishing: " +event);
		publisher.publishEvent(event);
	}

	public void publishDelete(AbstractEvent<Hotel> event) {
		System.out.println(" publisher publishing: " +event);
		publisher.publishEvent(event);
	}

}
