package commandservice.event.client;


import commandservice.event.AbstractEvent;

public interface EventPublisher<T> {

    void publish(AbstractEvent<T> event);
}
