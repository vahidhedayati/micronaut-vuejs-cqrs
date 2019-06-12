package hotel.write.event.client;

import hotel.write.domain.Hotel;
import hotel.write.event.AbstractEvent;
import hotel.write.event.client.eventStore.Data;
import hotel.write.event.client.eventStore.EventStoreClient;
import hotel.write.event.client.eventStore.HotelDataFactory;
import io.micronaut.context.annotation.Primary;

import javax.inject.Inject;

/**
 * This is EventStore publisher - implemented similar to kafka
 */

@Primary
public class EventStorePublisher implements EventPublisher<Hotel> {
	
	@Inject
	EventStoreClient<Hotel> eventClient;

	@Override
	public void publish(AbstractEvent<Hotel> event) {
		System.out.println(" EventStorePublisher publishing: " +event);

		//Data data = new HotelDataFactory().getData("hotelCreated", event.getEventCode(), event.getDtoFromEvent());
		//eventClient.sendEvent(data);
		eventClient.sendEvent("hotelCreated", event.getEventCode(), event);
	}
	@Override
	public void publishEdit(AbstractEvent<Hotel> event) {
		System.out.println(" EventStorePublisher publishing: " +event);
		//eventClient.sendEventEdit(event.getEventCode(), event);
		//Data data = new HotelDataFactory().getData("hotelEdit", event.getEventCode(), event.getDtoFromEvent());
		//eventClient.sendEvent(data);
		eventClient.sendEvent("hotelEdit",event.getEventCode(), event);
	}

	@Override
	public void publishDelete(AbstractEvent<Hotel> event) {
		System.out.println(" EventStorePublisher publishing: " +event);
		//eventClient.sendEventDelete(event.getEventCode(), event);
		//Data data = new HotelDataFactory().getData("hotelDelete", event.getEventCode(), event.getDtoFromEvent());
		//eventClient.sendEvent(data);
		eventClient.sendEvent("hotelDelete",event.getEventCode(), event);
	}
}
