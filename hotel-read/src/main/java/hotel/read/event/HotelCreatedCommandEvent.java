package hotel.read.event;

import hotel.read.adaptors.models.HotelCreatedCommand;

import java.io.Serializable;


public class HotelCreatedCommandEvent extends AbstractEvent<HotelCreatedCommand> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;

	private HotelCreatedCommand hotelCreatedCommand;

	public HotelCreatedCommandEvent() {

	}

	public HotelCreatedCommand getHotelCreatedCommand() {
		return hotelCreatedCommand;
	}

	public void setHotelCreatedCommand(HotelCreatedCommand hotelCreatedCommand) {
		this.hotelCreatedCommand = hotelCreatedCommand;
	}

	public HotelCreatedCommandEvent(HotelCreatedCommand hotel) {
		this.hotelCreatedCommand = hotel;
	}

	/**
	 * getEventId must be specific to kafka - fails due to null id
	 * @return
	 */
	@Override
	public String getEventId() {
		System.out.println("getEventId: " +  getDtoFromEvent());
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
		return this.hotelCreatedCommand;
	}

}
