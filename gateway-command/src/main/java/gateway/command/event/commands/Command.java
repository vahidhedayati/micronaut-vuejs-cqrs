package gateway.command.event.commands;


import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.websocket.WebSocketSession;

import java.time.Instant;
import java.util.UUID;

public abstract class Command implements Action {

    private String eventType;

    //Stores time of event
    private Instant instant;

    //Stores a random transaction Id
    private UUID transactionId;

    //Stores current hostname/port - for other useful stuff in future perhaps websocket connect back to this host
    private String host;
    private int port;


    //This needs to be provided with each form that submits an object that needs validation
    //follow hotelForm.vue which binds currentUser to hotelSaveCommand which extends this and sets this value when
    //created
    private String currentUser;

    /**
     * This is appended by gateway-command controller upon submission of a form if the form had currentUser defined
     */
    private WebSocketSession session;

    public void initiate( WebSocketSession session, EmbeddedServer embeddedServer, String eventType) {
        System.out.println("Got session "+session);
        this.session=session;
        this.eventType=eventType;
        this.instant = Instant.now();
        this.transactionId=UUID.randomUUID();
        this.host = embeddedServer.getHost();
        this.port = embeddedServer.getPort();
    }
    public void initiate(EmbeddedServer embeddedServer, String eventType) {
        this.eventType=eventType;
        this.instant = Instant.now();
        this.transactionId=UUID.randomUUID();
        this.host = embeddedServer.getHost();
        this.port = embeddedServer.getPort();
    }


    protected Command() {

    }

    public Command(Command cmd) {
        this.session=cmd.getSession();
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

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }
}