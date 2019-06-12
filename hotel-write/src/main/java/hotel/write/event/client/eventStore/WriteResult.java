package hotel.write.event.client.eventStore;

import akka.actor.Status;
import akka.actor.UntypedActor;
import eventstore.EsException;
import eventstore.WriteEventsCompleted;


public class WriteResult  extends UntypedActor {

    @SuppressWarnings("deprecation")
    public void onReceive(Object message) throws Exception {
        System.out.println("Receieved message"+message);
        if (message instanceof WriteEventsCompleted) {
            final WriteEventsCompleted completed = (WriteEventsCompleted) message;
            //log.info("range: {}, position: {}", completed.numbersRange(), completed.position());
        }
        else if (message instanceof Status.Failure) {
            final Status.Failure failure = ((Status.Failure) message);
            final EsException exception = (EsException) failure.cause();
            //log.error(exception, exception.toString());
        }
        else
        {
            unhandled(message);
        }
        context().system().shutdown();
    }

}