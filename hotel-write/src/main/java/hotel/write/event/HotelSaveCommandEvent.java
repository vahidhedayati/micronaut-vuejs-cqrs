package hotel.write.event;

import hotel.write.commands.HotelSaveCommand;

import java.io.Serializable;


public class HotelSaveCommandEvent extends AbstractEvent<HotelSaveCommand> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;

	private HotelSaveCommand hotelCreatedCommand;

	public HotelSaveCommandEvent() {

	}

	public HotelSaveCommand getHotelSaveCommand() {
		return hotelCreatedCommand;
	}

	public void setHotelSaveCommand(HotelSaveCommand hotelCreatedCommand) {
		this.hotelCreatedCommand = hotelCreatedCommand;
	}

	public HotelSaveCommandEvent(HotelSaveCommand hotel) {
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
	public HotelSaveCommand getDtoFromEvent() {
		return this.hotelCreatedCommand;
	}

}
