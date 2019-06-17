package hotel.write.commandHandlers;

import hotel.write.commands.*;
import hotel.write.commands.commandActions.CreateHotelCommand;
import hotel.write.commands.commandActions.DeleteHotelCommand;
import hotel.write.commands.commandActions.UpdateHotelCommand;
import hotel.write.domain.Hotel;
import hotel.write.event.*;
import hotel.write.event.client.EventPublisher;
import hotel.write.model.*;
import hotel.write.model.Command;
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
public class CreateHotelHandler extends AbstractCommandHandler<Hotel>{
	
	protected static final Logger LOG = LoggerFactory.getLogger(CreateHotelHandler.class);

	@Inject
	public CreateHotelHandler(Dao<Hotel> dao, EventPublisher<Hotel> publisher) {
		super(dao, publisher);
        System.out.println("Create Hotel handler");
	}

	@Override
	Hotel getDto(Command<Hotel> command) {
		Hotel hotel = ((CreateHotelCommand) command).getHotel();
		LOG.debug("hotel:" + hotel.getId() + "," + hotel.getName());
        System.out.println("hotel:" + hotel.getId() + "," + hotel.getName());
		return hotel;
	}

	@Override
	Hotel getUpd(Command<Hotel> command) {
		Hotel hotel = ((UpdateHotelCommand) command).getActualHotel();
		LOG.debug("hotel:" + hotel.getId() + "," + hotel.getName());
		System.out.println("getUpd getUpd getUpd hotel:" + hotel.getId() + "," + hotel.getName());
		return hotel;
	}

	@Override
	Hotel getDel(Command<Hotel> command) {
		Hotel hotel = ((DeleteHotelCommand) command).getActualHotel();
		LOG.debug("hotel:" + hotel.getId() + "," + hotel.getName());
		System.out.println("getUpd getUpd getUpd hotel:" + hotel.getId() + "," + hotel.getName());
		return hotel;
	}


	@Override
	AbstractEvent<Hotel> buildEvent(Hotel hotel) {
		return new HotelCreatedEvent(hotel);
	}

	@Override
    AbstractEvent<HotelCreatedCommand> buildEventFlexible(Hotel hotel) {
        System.out.println("buildEventFlexible build event hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelCreatedCommandEvent(new HotelCreatedCommand(hotel));
	}

    @Override
    AbstractEvent<HotelDeleteCommand> deleteEvent(Hotel hotel) {
        System.out.println("build deleteEvent hotel:" + hotel.getId() + "," + hotel.getName());
        return new HotelDeletedCommandEvent(new HotelDeleteCommand(hotel.getId()));
    }

    @Override
    AbstractEvent<HotelUpdateCommand> updateEvent(Hotel hotel) {
        System.out.println("build updateEvent1 event hotel:" + hotel.getId() + "," + hotel.getName());
        return new HotelUpdateCommandEvent(new HotelUpdateCommand(hotel));
    }
	AbstractEvent<HotelUpdateCommand> updateEvent(HotelUpdateCommand hotel) {
		System.out.println("build updateEvent2 event hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelUpdateCommandEvent(hotel);
	}

	@Override
	void save(Hotel hotel) {
        System.out.println("--- from abstract class save hotel:" + hotel.getId() + "," + hotel.getName());
		dao.save( hotel);
	}

	@Override
	void update(Hotel hotel) {
		System.out.println("--- from abstract class update hotel:" + hotel.getId() + "," + hotel.getName());
		dao.save( hotel);
	}
	void update(HotelUpdateCommand hotel) {
		System.out.println("--- from abstract class update HotelUpdateCommand:" + hotel.getId() + "," + hotel.getName());
		//dao.save( hotel);
	}

	@Override
	void updateCmd( Command<Hotel> command) {
		//System.out.println("--- from abstract class update HotelUpdateCommand:" + hotel.getId() + "," + hotel.getName());
		Hotel hotel = ((UpdateHotelCommand) command).getActualHotel();

		System.out.println("--- updateCmd  updateCmd updateCmd :" + hotel.getName() + "," + hotel.getCode());
		dao.update(hotel);
	}

	void delCmd(Command<Hotel> command) {
		Hotel hotel = ((DeleteHotelCommand) command).getActualHotel();
		System.out.println("--- updateCmd  updateCmd updateCmd :" + hotel.getName() + "," + hotel.getCode());
		dao.delete(hotel);
	}


	@Override
	void delete(Hotel dto) {

	}



	/*
	AbstractEvent<Hotel> saveCodeName(String code,String name) {
		return new HotelCreatedEvent(dao.addCodeName(code,name));
	}
	*/

	@Override
    Result<Hotel> buildResult(Hotel hotel) {
        System.out.println("buildResult hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelResult();
	}
}
