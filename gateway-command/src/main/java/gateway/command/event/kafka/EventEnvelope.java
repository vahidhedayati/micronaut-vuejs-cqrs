package gateway.command.event.kafka;

import gateway.command.event.commands.Command;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class EventEnvelope {


    private Command eventData;
    protected String eventType;

    protected EventEnvelope(String eventType,Command eventData) {
        this.eventData=eventData;
        this.eventType=eventType;
    }
}
