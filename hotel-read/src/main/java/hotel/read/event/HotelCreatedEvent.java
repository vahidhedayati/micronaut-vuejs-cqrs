package hotel.read.event;

import hotel.read.domain.Hotel;

import java.io.Serializable;


public class HotelCreatedEvent extends AbstractEvent<Hotel> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;
	
	private Hotel hotel;
	
	public HotelCreatedEvent() {

	}

	public HotelCreatedEvent(Hotel hotel) {
		this.hotel = hotel;
	}

	/**
	 * getEventId must be specific to kafka - fails due to null id
	 * @return
	 */
	@Override
	public String getEventId() {
		System.out.println("READ ---- getEventId: " +  getDtoFromEvent().getId());
		return getDtoFromEvent().getCode();
		//return getDtoFromEvent().getCode().toString();
	}
	@Override
	public String getEventCode() {
		System.out.println("READ -------- getEventCode: " +  getDtoFromEvent().getCode());
		return getDtoFromEvent().getCode();
	}
	@Override
	public Hotel getDtoFromEvent() {
		return this.hotel;
	}

}
