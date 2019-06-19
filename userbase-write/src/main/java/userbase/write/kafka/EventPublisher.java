package userbase.write.kafka;


import io.micronaut.runtime.server.EmbeddedServer;
import userbase.write.commands.Command;

public  interface EventPublisher {
    <T extends Command> void publish(EmbeddedServer embeddedServer, String topic, T command);
    <T extends Command> String serializeCommand(T command);
}