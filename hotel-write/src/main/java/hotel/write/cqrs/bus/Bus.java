package hotel.write.cqrs.bus;

import hotel.write.model.Command;
import hotel.write.model.CommandHandler;
import hotel.write.model.Result;

public interface Bus {

	public <R> Result<R> handleCommand(Command<R> command);
	
	public <R> void registerHandlerCommand(Command<R> command, CommandHandler<Command<R>, R> handler) ;

}
