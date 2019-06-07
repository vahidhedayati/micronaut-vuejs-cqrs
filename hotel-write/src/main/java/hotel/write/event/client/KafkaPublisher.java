package hotel.write.event.client;

import hotel.write.domain.Hotel;
import hotel.write.event.AbstractEvent;
import hotel.write.model.HotelSaveCommand;
import io.micronaut.context.annotation.Primary;

import javax.inject.Inject;

@Primary
public class KafkaPublisher implements EventPublisher<Hotel> {
	
	@Inject
	EventClient<Hotel> eventClient;

	@Override
	public void publish(AbstractEvent<Hotel> event) {
		System.out.println(" KafkaPublisher publishing: " +event);
		eventClient.sendEvent(event.getEventCode(), event);
	}

	@Override
	public void publishEdit(AbstractEvent<Hotel> event) {
		System.out.println(" KafkaPublisher publishing: " +event);
		eventClient.sendEventEdit(event.getEventCode(), event);
	}

	@Override
	public void publishDelete(AbstractEvent<Hotel> event) {
		System.out.println(" KafkaPublisher publishing: " +event);
		eventClient.sendEventDelete(event.getEventCode(), event);
	}
}
