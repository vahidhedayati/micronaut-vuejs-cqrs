package gateway.command.bus;

import gateway.command.event.commands.Command;

public interface CommandDispatcher {
    <T extends Command> void dispatch(T command);
}
