package gateway.command.event.commands;


import io.micronaut.runtime.server.EmbeddedServer;

import java.time.Instant;
import java.util.UUID;

public abstract class Command implements Action {

    private String eventType;


    //Stores time of event
    private Instant instant;

    //Stores a random transaction Id
    private String transactionId;

    //Stores current hostname/port - for other useful stuff in future perhaps websocket connect back to this host
    private String host;
    private int port;

    public void initiate(EmbeddedServer embeddedServer, String eventType) {
        this.eventType=eventType;
        this.instant = Instant.now();
        //We add eventType to transaction id - which is then parsed as part of the actual topic item key received
        this.transactionId=eventType+"_"+UUID.randomUUID().toString();
        this.host = embeddedServer.getHost();
        this.port = embeddedServer.getPort();
    }

    protected Command() {

    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}