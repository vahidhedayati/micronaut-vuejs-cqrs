package hotel.write.event;

import hotel.write.model.HotelCreatedCommand;

import java.io.Serializable;


public class HotelCreatedCommandEvent extends AbstractEvent<HotelCreatedCommand> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;

	private HotelCreatedCommand hotel;

	public HotelCreatedCommandEvent() {

	}

	public HotelCreatedCommandEvent(HotelCreatedCommand hotel) {
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
	public HotelCreatedCommand getDtoFromEvent() {
		return this.hotel;
	}

}
