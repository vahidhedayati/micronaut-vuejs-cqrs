package gateway.command.event.http;

import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.RxHttpClient;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class HttpClient {


    @Inject
    private RxHttpClient httpClient;

    private final String uri="/";
    HttpClient(RxHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    @Post("/")
    public <T extends CommandRoot> HttpResponse publish(T command) {
        return (HttpResponse)httpClient.toBlocking().retrieve(HttpRequest.POST("/", command), CommandRoot.class);
    }
   //
}
