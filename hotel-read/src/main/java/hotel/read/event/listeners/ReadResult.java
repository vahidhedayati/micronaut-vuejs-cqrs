package hotel.read.event.listeners;

import akka.actor.Status;
import akka.actor.UntypedActor;
import eventstore.EsException;
import eventstore.Event;
import eventstore.ReadEventCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadResult extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadResult.class);

    public void onReceive(Object message) throws Exception {
        LOGGER.info("Message received.");

        if (message instanceof ReadEventCompleted) {
            final ReadEventCompleted completed = (ReadEventCompleted) message;
            final Event event = completed.event();
            LOGGER.info("Event: [{}].", event);
        } else if (message instanceof Status.Failure) {
            final Status.Failure failure = ((Status.Failure) message);
            final EsException exception = (EsException) failure.cause();
            LOGGER.error("Failed status.  Message [{}] Exception [{}].", message, exception);
        } else {
            LOGGER.warn("Unknown message received [{}].", message);
            unhandled(message);
        }

        //context().system().shutdown();
    }

}
