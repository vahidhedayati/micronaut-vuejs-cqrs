package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;
import io.reactivex.Maybe;

import java.util.Set;

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

public class HotelListener extends HttpEventPublisher<HotelClient> {


    /**
     * This constructor is essential In gatewayController when : Class.forName(clazz.getName()).newInstance()
     * is called it needs a default constructor fall back on
     */
    public HotelListener() {
    }


    /**
     * Rather long winded but this now ties in to proper micronaut http client interface and does
     * its magic to send object over to remote receiving hotel-write application any active one via consul
     * @param clnt
     * @param command
     * @return HttpResponse from remote end
     */
    @Override
    public <T extends CommandRoot> HttpResponse publish(HotelClient clnt, T command) {
        return ((HotelClient)clnt).publish(command);
    }

}
