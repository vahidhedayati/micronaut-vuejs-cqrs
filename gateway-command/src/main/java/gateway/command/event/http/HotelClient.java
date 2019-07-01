package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

/**
 * Typical Micronaut HTTP Client passing real command object to remove client host:
 */
@Primary
@Client(id = "hotel-write", path = "/")
public interface HotelClient  extends  DefaultClient {

    @Post("/")
    <T extends  CommandRoot> Maybe<HttpResponse> publish(T command);
}