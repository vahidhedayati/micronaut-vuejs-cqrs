package hotel.write.event;

import hotel.write.model.HotelSaveCommand;

import java.io.Serializable;


public class HotelCreatedEvent extends AbstractEvent<HotelSaveCommand> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;
	
	private HotelSaveCommand hotel;
	
	public HotelCreatedEvent() {

	}

	public HotelCreatedEvent(HotelSaveCommand hotel) {
		this.hotel = hotel;
	}

	/**
	 * getEventId must be specific to kafka - fails due to null id
	 * @return
	 */
	@Override
	public String getEventId() {
		System.out.println("getEventId: " +  getDtoFromEvent().getId());
		return getDtoFromEvent().getCode();
		//return getDtoFromEvent().getCode().toString();
	}
	@Override
	public String getEventCode() {
		System.out.println("getEventCode: " +  getDtoFromEvent().getCode());
		return getDtoFromEvent().getCode();
	}
	@Override
	public HotelSaveCommand getDtoFromEvent() {
		return this.hotel;
	}

}
