package gateway.command.event.commands;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class DefaultEvent {

    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class);

    //Stores time of event
    private final Instant instant;
    //Stores a random transaction Id
    private final UUID transactionId;

    //Stores current hostname/port - for other useful stuff in future perhaps websocket connect back to this host
    protected final String host;
    protected final int port;

    protected String eventType;

    protected DefaultEvent() {
        instant = Instant.now();
        transactionId=UUID.randomUUID();
        host = embeddedServer.getHost();
        port = embeddedServer.getPort();
    }

    protected DefaultEvent(final Instant instant,final UUID transactionId, final String host, final int port) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DefaultEvent that = (DefaultEvent) o;

        return instant.equals(that.instant) &&
                transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return instant.hashCode()+
                transactionId.hashCode();
    }
}
