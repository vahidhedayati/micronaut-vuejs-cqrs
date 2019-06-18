package hotel.write.kafka;


import hotel.write.commands.Command;
import io.micronaut.runtime.server.EmbeddedServer;

public  interface EventPublisher {
    <T extends Command> void publish(EmbeddedServer embeddedServer, String topic, T command);
}