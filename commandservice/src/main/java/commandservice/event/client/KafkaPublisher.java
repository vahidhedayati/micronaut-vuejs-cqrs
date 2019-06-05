package commandservice.event.client;

import commandservice.event.AbstractEvent;
import commandservice.model.Hotel;
import commandservice.model.HotelSaveCommand;
import io.micronaut.context.annotation.Primary;

import javax.inject.Inject;

@Primary
public class KafkaPublisher implements EventPublisher<HotelSaveCommand> {
	
	@Inject
	EventClient<HotelSaveCommand> eventClient;

	@Override
	public void publish(AbstractEvent<HotelSaveCommand> event) {
		System.out.println(" KafkaPublisher publishing: " +event);
		eventClient.sendCode(event.getEventCode(), event);
	}
}
