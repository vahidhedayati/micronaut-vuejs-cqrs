package hotel.write.kafka;


import hotel.write.event.events.EventRoot;
import io.micronaut.runtime.server.EmbeddedServer;

public  interface EventPublisher {

    <T extends EventRoot> void publish(EmbeddedServer embeddedServer, String topic, T command);
}