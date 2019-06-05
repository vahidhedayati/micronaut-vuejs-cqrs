package hotel.write.event.client;

import hotel.write.event.AbstractEvent;
import hotel.write.model.HotelSaveCommand;
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
