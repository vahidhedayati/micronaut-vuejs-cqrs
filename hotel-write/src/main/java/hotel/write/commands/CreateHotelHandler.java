package hotel.write.commands;

import hotel.write.event.AbstractEvent;
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
public class CreateHotelHandler extends AbstractCommandHandler<HotelSaveCommand>{
	
	protected static final Logger LOG = LoggerFactory.getLogger(CreateHotelHandler.class);
	
	@Inject
	public CreateHotelHandler(Dao<HotelSaveCommand> dao, EventPublisher<HotelSaveCommand> publisher) {
		super(dao, publisher);
        System.out.println("Create Hotel handler");
	}

	@Override
	HotelSaveCommand getDto(Command<HotelSaveCommand> command) {
		HotelSaveCommand hotel = ((CreateHotelCommand) command).getHotel();
		LOG.debug("hotel:" + hotel.getId() + "," + hotel.getName());
        System.out.println("hotel:" + hotel.getId() + "," + hotel.getName());
		return hotel;
	}

	@Override
    AbstractEvent<HotelSaveCommand> buildEvent(HotelSaveCommand hotel) {
        System.out.println("build event hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelCreatedEvent(hotel);
	}

	@Override
	void save(HotelSaveCommand hotel) {
        System.out.println("--- from abstract class save hotel:" + hotel.getId() + "," + hotel.getName());
		dao.save( hotel);
	}

	@Override
    Result<HotelSaveCommand> buildResult(HotelSaveCommand hotel) {
        System.out.println("buildResult hotel:" + hotel.getId() + "," + hotel.getName());
		return new HotelResult();
	}
}
