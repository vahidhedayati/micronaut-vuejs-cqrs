package gateway.command.init;

import gateway.command.domain.Events;
import gateway.command.service.GatewayService;

import javax.inject.Inject;
import java.util.List;

public class RunnableEvents  implements Runnable {


    protected final GatewayService service;

    @Inject
    RunnableEvents(GatewayService service) {
        this.service = service;
    }

    @Override
    public void run() {

        List<Events> eventsList = service.processMissing();

        for (Events e:eventsList) {
            System.out.println("TODO Trying  to run outstanding event "+e.getEventType()+" "+e.getCommand());

        }
    }

}
