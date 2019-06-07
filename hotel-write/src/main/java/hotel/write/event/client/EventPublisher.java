package hotel.write.event.client;


import hotel.write.event.AbstractEvent;

public interface EventPublisher<T> {

    void publish(AbstractEvent<T> event);

    void publishEdit(AbstractEvent<T> event);

    void publishDelete(AbstractEvent<T> event);
}
