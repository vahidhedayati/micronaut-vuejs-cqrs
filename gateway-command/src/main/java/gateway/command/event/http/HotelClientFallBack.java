package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.Maybe;

@Client(id = "hotel-write", path = "/")
@Fallback
public class HotelClientFallBack implements HotelClient {

    /**
     * Issue getting through to real HOTEL-WRITE APP
     * @param command
     * @param <T>
     * @return
     */
   @Post("/")
   public <T extends  CommandRoot> Maybe<HttpResponse> publish(T command) {
       System.out.println("fall back");
        return Maybe.just(HttpResponse.serverError());
    }

}