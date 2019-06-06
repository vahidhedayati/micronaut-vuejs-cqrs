package hotel.write.model;

public interface CommandHandler<C extends Command<R>, R> {
	Result<R> handleCommand(Command<R> command);

	//Result<R> addCodeName(Command<R> command);
}
