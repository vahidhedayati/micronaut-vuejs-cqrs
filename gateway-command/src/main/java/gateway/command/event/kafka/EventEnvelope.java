package gateway.command.event.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.commands.Command;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class EventEnvelope {

    protected String eventType;
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    //Stores time of event
    private final Instant instant;
    //Stores a random transaction Id
    private final UUID transactionId;

    //Stores current hostname/port - for other useful stuff in future perhaps websocket connect back to this host
    protected final String host;
    protected final int port;

    private Command eventData;


    protected EventEnvelope() {
        instant = Instant.now();
        transactionId=UUID.randomUUID();
        host = embeddedServer.getHost();
        port = embeddedServer.getPort();
    }

    protected EventEnvelope(final Instant instant,final UUID transactionId, final String host, final int port) {
        Objects.requireNonNull(instant);
        this.instant = instant;
        this.transactionId=transactionId;
        this.host=host;
        this.port=port;
    }

    public Instant getInstant() {
        return instant;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    protected EventEnvelope(String eventType,Command eventData) {
        this.eventData=eventData;
        this.eventType=eventType;
        instant = Instant.now();
        transactionId=UUID.randomUUID();
        host = embeddedServer.getHost();
        port = embeddedServer.getPort();
    }
}
