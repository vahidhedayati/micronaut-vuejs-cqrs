package commandservice.event.client;

import commandservice.event.AbstractEvent;
import commandservice.model.Hotel;
import commandservice.model.HotelSaveCommand;
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
