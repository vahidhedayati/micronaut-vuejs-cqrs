package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;
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
@Singleton
public class HotelListener extends HttpEventPublisher<HotelClient> {
    @Inject
    @Client(id = "hotel-write")
    private   RxHttpClient httpClient;
    public HotelListener() {

    }
    public HotelListener(  @Client(id = "hotel-write") RxHttpClient httpClient) {
        super(httpClient);
        //this.httpClient=httpClient;
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
    @Override
    public <T extends CommandRoot> HttpResponse publish(T command) {
       // return (HttpResponse)httpClient.toBlocking().retrieve(HttpRequest.POST("/", command), CommandRoot.class);
        return   publishIt(httpClient,command); // publishToHttp(getHttpClient(),command);
    }
    @Override
    public <T extends CommandRoot> HttpResponse publish(@Client(id = "hotel-write") RxHttpClient httpClient, T command) {
        System.out.println(" Attempting cmd "+command);
        // httpClient.retrieve(HttpRequest.POST("/", command), CommandRoot.class);
        // return (HttpResponse)httpClient.toBlocking().retrieve(HttpRequest.POST("/", command), CommandRoot.class);
        return  publishIt(httpClient,command);
    }
    public <T extends CommandRoot> HttpResponse publishToHttp(   @Client(id = "hotel-write") RxHttpClient httpClient , T command) {
        return publishIt(httpClient,command);
    }
}
