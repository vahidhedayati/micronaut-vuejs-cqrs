package hotel.write.event;

import hotel.write.commands.HotelDeleteCommand;

import java.io.Serializable;


public class HotelDeletedCommandEvent extends AbstractEvent<HotelDeleteCommand> implements  Serializable{

	private static final long serialVersionUID = -7452000227812130114L;

	private HotelDeleteCommand command;

	public HotelDeletedCommandEvent() {

	}

	public HotelDeleteCommand getCommand() { return this.command; }

	public void setCommand(HotelDeleteCommand command) {
		this.command = command;
	}

	public HotelDeletedCommandEvent(HotelDeleteCommand hotel) {
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
	public HotelDeleteCommand getDtoFromEvent() {
		return this.command;
	}

}
