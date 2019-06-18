package hotel.write.commandHandlers;

import hotel.write.kafka.EventPublisher;

import hotel.write.model.Command;
import hotel.write.model.CommandHandler;
import hotel.write.model.Result;
import io.micronaut.runtime.server.EmbeddedServer;
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

	protected static final String topic = "hotelRead";

	//protected Dao<T> dao;
	//private EventPublisher<T> publisher;

	private final EventPublisher publisher;
	private final EmbeddedServer embeddedServer;

	public AbstractCommandHandler(EventPublisher eventPublisher, EmbeddedServer embeddedServer) {
		this.publisher = eventPublisher;
		this.embeddedServer=embeddedServer;
	}

	@Override
	public Result<T> handleCommand(Command<T> command) {
		T dto = getDto(command);
		System.out.println("handleCommand AbstractCommandHandler - save is called "+dto);
		save(dto);
		System.out.println("handleCommand AbstractCommandHandler - buildEvent -------- WRITE ---------------------------------------- "+dto);
		hotel.write.commands.Command cmd = (hotel.write.commands.Command) dto;
		publisher.publish(embeddedServer,topic,cmd);
		return buildResult(dto);
	}

	abstract void save(T dto);
	abstract T getDto(Command<T> command);

	abstract Result<T> buildResult(T dto);

}
