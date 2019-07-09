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

@Singleton
public class UserListener extends HttpEventPublisher<UserClient> {
    @Inject
    @Client(id = "userbase-write")
    private   RxHttpClient httpClient;
    /**
     * This constructor is essential In gatewayController when : Class.forName(clazz.getName()).newInstance()
     * is called it needs a default constructor fall back on
     */
    public UserListener(  @Client(id = "userbase-write") RxHttpClient httpClient) {
        super(httpClient);
        //this.httpClient=httpClient;
    }
    public UserListener() {}

    /**
     * Rather long winded but this now ties in to proper micronaut http client interface and does
     * its magic to send object over to remote receiving hotel-write application any active one via consul
     * @param clnt
     * @param command
     * @return HttpResponse from remote end
     */
    @Override
    public <T extends CommandRoot> HttpResponse publish(UserClient clnt, T command) {
        return ((UserClient)clnt).publish(command);
    }

    @Override
    public <T extends CommandRoot> HttpResponse publish( T command) {
        System.out.println(" Attempting cmd "+command);
       // httpClient.retrieve(HttpRequest.POST("/", command), CommandRoot.class);
       // return (HttpResponse)httpClient.toBlocking().retrieve(HttpRequest.POST("/", command), CommandRoot.class);
        return  publishIt(httpClient,command);
    }

    @Override
    public <T extends CommandRoot> HttpResponse publish(@Client(id = "userbase-write") RxHttpClient httpClient, T command) {
        System.out.println(" Attempting cmd "+command);
        // httpClient.retrieve(HttpRequest.POST("/", command), CommandRoot.class);
        // return (HttpResponse)httpClient.toBlocking().retrieve(HttpRequest.POST("/", command), CommandRoot.class);
        return  publishIt(httpClient,command);
    }

    public <T extends CommandRoot> HttpResponse publishToHttp(   @Client(id = "userbase-write") RxHttpClient httpClient , T command) {
        return publishIt(httpClient,command);
    }
}
