package gateway.command.event;

import io.micronaut.context.event.ApplicationEvent;

public class ProcessEvent extends ApplicationEvent {

    public ProcessEvent(Object source) {
        super(source);
    }
}
