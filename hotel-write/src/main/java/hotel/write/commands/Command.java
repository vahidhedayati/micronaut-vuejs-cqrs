package hotel.write.commands;


public abstract class Command implements Action {

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    private String eventType;


}