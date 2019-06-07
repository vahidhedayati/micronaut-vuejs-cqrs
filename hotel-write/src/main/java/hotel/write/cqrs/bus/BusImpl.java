package hotel.write.cqrs.bus;

import hotel.write.commands.CreateHotelCommand;
import hotel.write.domain.Hotel;
import hotel.write.model.Command;
import hotel.write.model.CommandHandler;
import hotel.write.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class BusImpl implements Bus {
	
	protected static final Logger LOG = LoggerFactory.getLogger(BusImpl.class);
	
	private Map<String, CommandHandler<?, ?>> handlers = new HashMap<>();
	
	@Inject 
	public BusImpl(CommandHandler<Command<Hotel>, Hotel> handler) {
		handlers.put(CreateHotelCommand.class.getSimpleName(), handler );
	}

	@SuppressWarnings("unchecked")
	public <R> Result<R> handleCommand(Command<R> command) {
		LOG.debug("handle command: " + command.getCommandName());
		System.out.println("handle command: " + command.getCommandName());
		CommandHandler<Command<R>, R> handler = (CommandHandler<Command<R>, R>) handlers.get(command.getCommandName());
		if (handler!=null) {
			return (Result<R>) handler.handleCommand(command);
		} else {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public <R> Result<R> updateCommand(Command<R> command) {
		LOG.debug("handle updateCommand: " + command.getCommandName());
		System.out.println("handle updateCommand: " + command.getCommandName());
		CommandHandler<Command<R>, R> handler = (CommandHandler<Command<R>, R>) handlers.get(command.getCommandName());
		if (handler!=null) {
			return (Result<R>) handler.updateCommand(command);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public <R> Result<R> deleteCommand(Command<R> command) {
		LOG.debug("handle deleteCommand: " + command.getCommandName());
		System.out.println("handle deleteCommand: " + command.getCommandName());
		CommandHandler<Command<R>, R> handler = (CommandHandler<Command<R>, R>) handlers.get(command.getCommandName());
		if (handler!=null) {
			return (Result<R>) handler.deleteCommand(command);
		} else {
			return null;
		}

	}
	
	public <R> void registerHandlerCommand(Command<R> command, CommandHandler<Command<R>, R> handler)  {
		handlers.putIfAbsent(command.getCommandName(), handler);
	}
}
