package userbase.write.event.kafka;


import io.micronaut.runtime.server.EmbeddedServer;
import userbase.write.event.events.EventRoot;

public  interface EventPublisher {
    <T extends EventRoot> void publish(EmbeddedServer embeddedServer, String topic, T command);
}