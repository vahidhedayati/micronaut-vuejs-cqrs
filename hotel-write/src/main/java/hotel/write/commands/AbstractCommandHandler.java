package hotel.write.commands;

import hotel.write.event.AbstractEvent;
import hotel.write.event.client.EventPublisher;
import hotel.write.model.Command;
import hotel.write.model.CommandHandler;
import hotel.write.model.Result;
import hotel.write.services.write.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This drives the logic behind CreateHotelHandler
 * implements CommandHandler<Command<
 *
 * @param <T>
 */
public abstract class AbstractCommandHandler<T> implements CommandHandler<Command<T>, T> {

	protected static final Logger LOG = LoggerFactory.getLogger(AbstractCommandHandler.class);

	protected Dao<T> dao;

	private EventPublisher<T> publisher;

	public AbstractCommandHandler(Dao<T> dao, EventPublisher<T> publisher) {
		this.dao = dao;
		this.publisher = publisher;
	}


	@Override
	public Result<T> handleCommand(Command<T> command) {
		T dto = getDto(command);
		System.out.println("handleCommand AbstractCommandHandler - save is called "+dto);
		save(dto);
		System.out.println("handleCommand AbstractCommandHandler - buildEvent "+dto);
		publish( buildEvent(dto));
		return buildResult(dto);
	}

	/*
	@Override
	public Result<T> addCodeName(Command<T> command) {
		T dto = getDto(command);
		save(dto);
		//T dto = saveCodeName(command);
		System.out.println("handleCommand AbstractCommandHandler - buildEvent "+dto);
		publish( buildEvent(dto));
		return buildResult(dto);
	}
	*/


	abstract void save(T dto);
	abstract T getDto(Command<T> command);

	//abstract T saveCodeName(Command<T> command);

	abstract AbstractEvent<T> buildEvent(T dto);

	void publish(AbstractEvent<T> event) {
		if (event != null) {
			System.out.println("publisher.publish("+event);
			publisher.publish(event);
		}
	}

	abstract Result<T> buildResult(T dto);

}
