package hotel.write.event;


import hotel.write.kafka.EventEnvelope;

import java.io.Serializable;


public class HotelCommandEvent extends AbstractEvent<EventEnvelope> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;

	private EventEnvelope eventEnvelope;

	public HotelCommandEvent() {

	}

	public EventEnvelope getEventEnvelope() {
		return eventEnvelope;
	}

	public void setEventEnvelope(EventEnvelope eventEnvelope) {
		this.eventEnvelope = eventEnvelope;
	}

	public HotelCommandEvent(EventEnvelope eventEnvelope) {
		this.eventEnvelope = eventEnvelope;
	}

	/**
	 * getEventId must be specific to kafka - fails due to null id
	 * @return
	 */
	@Override
	public String getEventId() {
		System.out.println("getEventId: " +  getDtoFromEvent());
		return getDtoFromEvent().getEventType();
		//return getDtoFromEvent().getCode().toString();
	}

	@Override
	public String getEventCode() {
		System.out.println("getEventCode: " +  getDtoFromEvent().getEventType());
		return getDtoFromEvent().getEventType().toString();
	}
	@Override
	public EventEnvelope getDtoFromEvent() {
		return this.eventEnvelope;
	}

}
