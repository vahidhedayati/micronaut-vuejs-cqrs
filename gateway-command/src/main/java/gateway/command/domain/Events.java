package gateway.command.domain;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "event_type", length = 155)
    String eventType;

    @Column(name = "command",  columnDefinition="TEXT")
    String command;

    @Column(name = "processed")
    Date processed;

    public Events() {}
    public Events(Date date, String eventType, String command) {
        this.date = date;
        this.eventType = eventType;
        this.command = command;

    }
    public Events(Date date, String eventType, String command, Date processed) {
        this.date = date;
        this.eventType = eventType;
        this.command = command;
        this.processed = processed;
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

}
