package hotel.read.event;

import hotel.read.adaptors.models.HotelUpdateCommand;

import java.io.Serializable;


public class HotelUpdateCommandEvent extends AbstractEvent<HotelUpdateCommand> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;

	private HotelUpdateCommand command;

	public HotelUpdateCommandEvent() {

	}

	public HotelUpdateCommand getCommand() { return this.command; }

	public void setCommand(HotelUpdateCommand command) {
		this.command = command;
	}

	public HotelUpdateCommandEvent(HotelUpdateCommand hotel) {
		this.command = hotel;
	}

	/**
	 * getEventId must be specific to kafka - fails due to null id
	 * @return
	 */
	@Override
	public String getEventId() {
		System.out.println("getEventId: " +  getDtoFromEvent());
		return getDtoFromEvent().getId().toString();
	}
	@Override
	public String getEventCode() {
		System.out.println("getEventCode: " +  getDtoFromEvent().getId());
		return getDtoFromEvent().getId().toString();
	}
	@Override
	public HotelUpdateCommand getDtoFromEvent() {
		return this.command;
	}

}
