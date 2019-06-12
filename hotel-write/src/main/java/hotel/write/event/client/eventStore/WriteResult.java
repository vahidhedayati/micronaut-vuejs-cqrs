package hotel.write.event.client.eventStore;

import akka.actor.Status;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import eventstore.EsException;
import eventstore.EventNumber;
import eventstore.WriteEventsCompleted;


public class WriteResult  extends UntypedActor {
    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public void onReceive(Object message) throws Exception {
        if (message instanceof WriteEventsCompleted) {
            final WriteEventsCompleted completed = (WriteEventsCompleted) message;
            //final EventNumber.Exact eventNumber = completed.firstEventNumber();
            //log.info("eventNumber: {}", eventNumber);
        } else if (message instanceof Status.Failure) {
            final Status.Failure failure = ((Status.Failure) message);
            final EsException exception = (EsException) failure.cause();
            //log.error("reason: {}, message: {}", exception.reason(), exception.message());
        } else
            unhandled(message);

        //context().system().shutdown();
    }

}