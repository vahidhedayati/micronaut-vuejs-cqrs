package hotel.write.event.client;


import hotel.write.event.AbstractEvent;

public interface EventPublisher<T> {

    void publish(AbstractEvent<T> event);

}
