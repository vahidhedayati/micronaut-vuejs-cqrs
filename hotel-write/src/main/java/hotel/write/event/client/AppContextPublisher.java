package hotel.write.event.client;

import hotel.write.event.AbstractEvent;
import hotel.write.model.HotelSaveCommand;
import io.micronaut.context.event.ApplicationEventPublisher;

import javax.inject.Inject;

public class AppContextPublisher implements EventPublisher<HotelSaveCommand> {
	
	@Inject
	ApplicationEventPublisher publisher;

	@Override
	public void publish(AbstractEvent<HotelSaveCommand> event) {
		System.out.println(" publisher publishing: " +event);
		publisher.publishEvent(event);
	}
}
