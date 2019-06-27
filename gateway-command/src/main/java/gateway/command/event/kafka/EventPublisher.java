package gateway.command.event.kafka;

import gateway.command.event.commands.CommandRoot;
import io.micronaut.runtime.server.EmbeddedServer;

public  interface EventPublisher {
    <T extends CommandRoot> void publish(EmbeddedServer embeddedServer, String topic, T command);
}