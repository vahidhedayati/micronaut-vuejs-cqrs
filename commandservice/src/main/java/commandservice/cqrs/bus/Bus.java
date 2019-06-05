package commandservice.cqrs.bus;

import commandservice.model.Command;
import commandservice.model.CommandHandler;
import commandservice.model.Result;

public interface Bus {

	public <R> Result<R> handleCommand(Command<R> command);
	
	public <R> void registerHandlerCommand(Command<R> command, CommandHandler<Command<R>, R> handler) ;

}
