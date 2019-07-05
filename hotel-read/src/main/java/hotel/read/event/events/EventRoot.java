package hotel.read.event.events;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import hotel.read.event.Action;
import hotel.read.event.commands.CommandRoot;
import io.micronaut.runtime.server.EmbeddedServer;

import java.time.Instant;
import java.util.UUID;

/**
 * Please note abstract classes as json deserialization goes runs into issues, to get around it this block is needed:
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, //field must be present in the POJO
        property = "eventType")

@JsonSubTypes({
        @JsonSubTypes.Type(value = HotelCreated.class),
        @JsonSubTypes.Type(value = HotelSaved.class),
        @JsonSubTypes.Type(value = HotelUpdated.class),
        @JsonSubTypes.Type(value = HotelDeleted.class),
        @JsonSubTypes.Type(value = UserSaved.class),
        @JsonSubTypes.Type(value = UserDeleted.class),
        @JsonSubTypes.Type(value = UserUpdated.class)
})
public abstract class EventRoot implements Action {

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

    public void initiate(EmbeddedServer embeddedServer, String eventType, String topic) {
        this.topic=topic;
        this.eventType=eventType;
        this.instant = Instant.now();
        this.transactionId=UUID.randomUUID();
        this.host = embeddedServer.getHost();
        this.port = embeddedServer.getPort();
    }

    protected EventRoot() {}

    public EventRoot(EventRoot cmd) {
        this.topic=cmd.getTopic();
        this.currentUser=cmd.getCurrentUser();
        this.eventType=cmd.getEventType();
        this.instant=cmd.getInstant();
        this.transactionId=cmd.getTransactionId();
        this.host=cmd.getHost();
        this.port=cmd.getPort();
    }
    public EventRoot(CommandRoot cmd) {
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