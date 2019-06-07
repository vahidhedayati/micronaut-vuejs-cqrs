package hotel.write.commands;

import hotel.write.domain.Hotel;
import hotel.write.event.AbstractEvent;
import hotel.write.event.HotelCreatedCommandEvent;
import hotel.write.event.HotelCreatedEvent;
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
	AbstractEvent<Hotel> buildEvent(Hotel hotel) {
		return new HotelCreatedEvent(hotel);
	}

	@Override
    AbstractEvent<HotelCreatedCommand> buildEventFlexible(Hotel hotel) {
        System.out.println("build event hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelCreatedCommandEvent(new HotelCreatedCommand(hotel));
	}

	@Override
	void save(Hotel hotel) {
        System.out.println("--- from abstract class save hotel:" + hotel.getId() + "," + hotel.getName());
		dao.save( hotel);
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
