package gateway.command.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * This is a domain class that captures any events / commands that could not be sent out due to http / errors
 *
 * an additional process is required to run through outstanding events.
 */
@Entity
@Table(name = "events")
public class  Events {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    Date date;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "topic")
    private String topic;

    //This stores the HTTP client used to process command
    @Column(name = "client")
    String client;

    @Column(name = "event_type", length = 155)
    String eventType;

    @Column(name = "command",  columnDefinition="TEXT")
    String command;

    @Column(name = "processed")
    Date processed;

    public Events() {}


    public Events(Date date, String client, String topic,  String transactionId, String eventType, String command) {
        this.client=client;
        this.topic=topic;
        this.date = date;
        this.transactionId = transactionId;
        this.eventType = eventType;
        this.command = command;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Date getProcessed() {
        return processed;
    }

    public void setProcessed(Date processed) {
        this.processed = processed;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
