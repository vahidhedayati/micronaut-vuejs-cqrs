package hotel.write.event.kafka;

import hotel.write.commands.Command;

public class EventEnvelope {


    private Command eventData;
    protected String eventType;

    protected EventEnvelope(String eventType,Command eventData) {
        this.eventData=eventData;
        this.eventType=eventType;
    }
}
