package commandservice.model;

public interface CommandHandler<C extends Command<R>, R> {
	Result<R> handleCommand(Command<R> command);
}
