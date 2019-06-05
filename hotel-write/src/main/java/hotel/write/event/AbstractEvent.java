package hotel.write.event;

public abstract class  AbstractEvent<T> {
	
	public abstract String getEventId();

	public abstract String getEventCode();
	public abstract T getDtoFromEvent();
		
}
