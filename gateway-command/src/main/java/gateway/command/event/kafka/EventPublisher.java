package gateway.command.event.kafka;

import gateway.command.event.commands.Command;

public  interface EventPublisher {
    <T extends Command> void publish(String topic, T command);
}