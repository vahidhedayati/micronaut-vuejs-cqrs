package gateway.command.event.http;

import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Maybe;

import javax.inject.Inject;
import java.util.Set;

/**
 * This is a generic abstract class extended to be used by gateway to be able to call any extended classes of this
 * and trigger its publish method
 */


public abstract class HttpEventPublisher<R> {

    @Inject
    private   RxHttpClient httpClient;
    public HttpEventPublisher() { }
    public HttpEventPublisher(RxHttpClient httpClient) { this.httpClient=httpClient; }


    public abstract<T extends CommandRoot> HttpResponse publish(R clnt, T command);

    public abstract<T extends CommandRoot> HttpResponse publish( T command);

    public abstract<T extends CommandRoot> HttpResponse publish( RxHttpClient httpClient, T command);

    public <T extends CommandRoot> HttpResponse publishIt( RxHttpClient httpClient, T command) {
        //this.httpClient=hc;
        System.out.println(" Attempting publishIt "+command);
       // httpClient.retrieve(HttpRequest.POST("/", command), CommandRoot.class);
        System.out.println(" Attempting publishIt 2 "+command);
        return (HttpResponse)httpClient.toBlocking().retrieve(HttpRequest.POST("/", command), CommandRoot.class);
    }

    public RxHttpClient getHttpClient() {
        return httpClient;
    }
}
