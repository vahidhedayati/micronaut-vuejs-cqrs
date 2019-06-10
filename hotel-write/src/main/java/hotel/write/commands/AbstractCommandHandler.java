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
		System.out.println("handleCommand AbstractCommandHandler - buildEvent -------- WRITE ---------------------------------------- "+dto);
		//publish( buildEventFlexible(dto));
        publish( buildEventFlexible(dto));
		return buildResult(dto);
	}

	@Override
	public Result updateCommand(Command<T> command) {
		T dto = getUpd(command);
		System.out.println("handleCommand AbstractCommandHandler - save is called  get Command name "+command.getCommandName());
		updateCmd(command);
		System.out.println("handleCommand AbstractCommandHandler - buildEvent -------- WRITE ---------------------------------------- ");
        publishEdit( updateEvent(dto));
		//publish(buildEvent(dto));

		System.out.println("handleCommand AbstractCommandHandler - Dto dto "+dto);
		return buildResult(dto);
	}
	@Override
	public Result<T> deleteCommand(Command<T> command) {
		T dto = getDel(command);
		System.out.println("handleCommand AbstractCommandHandler - save is called "+dto);
		System.out.println("handleCommand AbstractCommandHandler - buildEvent -------- WRITE ---------------------------------------- "+dto);
		//publish( buildEventFlexible(dto));
        publishDelete(deleteEvent(dto));

		delCmd(command);

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

	abstract void update(T dto);
	abstract void updateCmd(Command<T> command);
    abstract void delCmd(Command<T> command);
	abstract void delete(T dto);

	abstract void save(T dto);
	abstract T getDto(Command<T> command);
	abstract T getUpd(Command<T> command);
    abstract T getDel(Command<T> command);
	//abstract T saveCodeName(Command<T> command);

	abstract AbstractEvent<T> buildEvent(T dto);

	abstract AbstractEvent buildEventFlexible(T dto);

	abstract AbstractEvent deleteEvent(T dto);

	abstract AbstractEvent updateEvent(T dto);

	void publish(AbstractEvent<T> event) {
		if (event != null) {
			System.out.println("publisher.publish("+event);
			publisher.publish(event);
		}
	}
    void publishEdit(AbstractEvent<T> event) {
        if (event != null) {
            System.out.println("publisher.publish("+event);
            publisher.publishEdit(event);
        }
    }
    void publishDelete(AbstractEvent<T> event) {
        if (event != null) {
            System.out.println("publisher.publish("+event);
            publisher.publishDelete(event);
        }
    }
	abstract Result<T> buildResult(T dto);

}
