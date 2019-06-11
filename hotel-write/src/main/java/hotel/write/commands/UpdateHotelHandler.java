package hotel.write.commands;

import hotel.write.domain.Hotel;
import hotel.write.event.*;
import hotel.write.event.client.EventPublisher;
import hotel.write.model.*;
import hotel.write.services.write.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This calls the EventPublisher - which is kafka client to push a kafka event out.
 *
 */
@Singleton
public class UpdateHotelHandler extends AbstractCommandHandler<HotelUpdateCommand>{

	protected static final Logger LOG = LoggerFactory.getLogger(UpdateHotelHandler.class);

	@Inject
	public UpdateHotelHandler(Dao<HotelUpdateCommand> dao, EventPublisher<HotelUpdateCommand> publisher) {
		super(dao, publisher);
        System.out.println("Create Hotel handler");
	}

	@Override
	HotelUpdateCommand getDto(Command<HotelUpdateCommand> command) {
		HotelUpdateCommand hotel = ((HotelUpdateCommand) command);
		LOG.debug("HotelUpdateCommand:" + hotel.getId() + "," + hotel.getName());
        System.out.println("HotelUpdateCommand:" + hotel.getId() + "," + hotel.getName());
		return hotel;
	}

	@Override
	HotelUpdateCommand getUpd(Command<HotelUpdateCommand> command) {
		return null;
	}

	@Override
	HotelUpdateCommand getDel(Command<HotelUpdateCommand> command) {
		return null;
	}


	@Override
	AbstractEvent<HotelUpdateCommand> buildEventFlexible(HotelUpdateCommand hotel) {
		System.out.println("UpdateHotelHandler build event hotel:" + hotel.getId() + "," + hotel.getName());
		//return new hotel; // HotelUpdateCommand(hotel);
		return new HotelUpdateCommandEvent(hotel);
	}

	@Override
	AbstractEvent<HotelUpdateCommand> deleteEvent(HotelUpdateCommand hotel) {
		System.out.println("UpdateHotelHandler HotelUpdateCommand deleteEvent build deleteEvent hotel:" + hotel.getId() + "," + hotel.getName());
		//return new HotelDeletedCommandEvent(new HotelDeleteCommand(hotel.getId()));
		return new HotelUpdateCommandEvent(hotel);
	}

	@Override
	AbstractEvent<HotelUpdateCommand> updateEvent(HotelUpdateCommand hotel) {
		System.out.println("UpdateHotelHandler HotelUpdateCommand updateEvent  build event hotel:" + hotel.getId() + "," + hotel.getName());
		//return new HotelUpdateCommandEvent(new HotelUpdateCommand(hotel));
		return new HotelUpdateCommandEvent(hotel);
	}

	@Override
	AbstractEvent<HotelUpdateCommand> buildEvent(HotelUpdateCommand hotel) {
		System.out.println("UpdateHotelHandler HotelUpdateCommand buildEvent build event hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelUpdateCommandEvent(hotel);
	}


	@Override
	void save(HotelUpdateCommand hotel) {
        System.out.println("UpdateHotelHandler --- from abstract class save hotel:" + hotel.getId() + "," + hotel.getName());
		dao.save( hotel);
	}


	@Override
	void update(HotelUpdateCommand hotel) {
		System.out.println("UpdateHotelHandler --- from abstract class save hotel:" + hotel.getId() + "," + hotel.getName());
		dao.save( hotel);
	}

	@Override
	void updateCmd(Command<HotelUpdateCommand> command) {

	}

	@Override
	void delCmd(Command<HotelUpdateCommand> command) {

	}

	@Override
	void delete(HotelUpdateCommand dto) {
		System.out.println("UpdateHotelHandler --- from abstract class delete hotel:" + dto.getId() + "," + dto.getName());
	}



	/*
	AbstractEvent<Hotel> saveCodeName(String code,String name) {
		return new HotelCreatedEvent(dao.addCodeName(code,name));
	}
	*/

	@Override
    Result<HotelUpdateCommand> buildResult(HotelUpdateCommand hotel) {
        System.out.println("UpdateHotelHandler buildResult hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelUpdateResult();
	}
}
