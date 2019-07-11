package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;

import javax.inject.Inject;

/**
 * This class is the overall dynamic binding class of HttpPublished -
 * When a user posts json strings it has what topic or subject which gets converted from
 *
 * user to UserListener
 * hotel to HotelListener
 *
 * Gateway controller loads in the class by name and it is mimicked and
 * publish action of this class is called via {@link HttpEventPublisher } within gatewaycontroller
 *
 *
 *
 */

public class HotelListener extends HttpEventPublisher {

    @Inject
    private  HotelClient client;

    /**
     * This constructor is essential In gatewayController when : Class.forName(clazz.getName()).newInstance()
     * is called it needs a default constructor fall back on
     */
    public HotelListener() { }


    @Override
    public <T extends CommandRoot> HttpResponse publish( T command) {
        return client.publish(command);
    }

}
