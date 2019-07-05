package hotel.write.event.commands;



import hotel.write.event.Action;
import hotel.write.event.events.EventRoot;
import io.micronaut.runtime.server.EmbeddedServer;

import java.time.Instant;
import java.util.UUID;


public abstract class CommandRoot implements Action {

    private String eventType;

    //Stores time of event
    private Instant instant;

    private String topic;
    //Stores a random transaction Id
    private UUID transactionId;

    //Stores current hostname/port - for other useful stuff in future perhaps websocket connect back to this host
    private String host;
    private int port;


    //This needs to be provided with each form that submits an object that needs validation
    //follow hotelForm.vue which binds currentUser to hotelSaveCommand which extends this and sets this value when
    //created
    private String currentUser;

    public void initiate(EmbeddedServer embeddedServer, String eventType) {
        this.eventType=eventType;
        this.instant = Instant.now();
        this.transactionId=UUID.randomUUID();
        this.host = embeddedServer.getHost();
        this.port = embeddedServer.getPort();
    }
    public void initiate(EmbeddedServer embeddedServer, String eventType, String topic) {
        this.topic=topic;
        this.eventType=eventType;
        this.instant = Instant.now();
        this.transactionId=UUID.randomUUID();
        this.host = embeddedServer.getHost();
        this.port = embeddedServer.getPort();
    }
    protected CommandRoot() {}


    public CommandRoot(EventRoot cmd) {
        this.topic=cmd.getTopic();
        this.currentUser=cmd.getCurrentUser();
        this.eventType=cmd.getEventType();
        this.instant=cmd.getInstant();
        this.transactionId=cmd.getTransactionId();
        this.host=cmd.getHost();
        this.port=cmd.getPort();
    }


    public CommandRoot(CommandRoot cmd) {
        this.topic=cmd.getTopic();
        this.currentUser=cmd.getCurrentUser();
        this.eventType=cmd.getEventType();
        this.instant=cmd.getInstant();
        this.transactionId=cmd.getTransactionId();
        this.host=cmd.getHost();
        this.port=cmd.getPort();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
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

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}