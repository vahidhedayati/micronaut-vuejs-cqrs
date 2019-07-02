package gateway.command.event.http;

import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.reactivex.Maybe;

/**
 * A generic interface which is extended by real Micronaut Http Client interfaces.
 *
 * This is the overall global class mapped on Gatewaycontroller when submitting a tast To any of the Listeners
 * HotelListener.java as an example converts the default passed in constructed DefaultClient and converts to HotelClient interface
 *
 * This is a way of getting gateway to just dynamically pass this default interface which converts locally to real interface
 */
public interface DefaultClient  {

    @Post("/")
    <T extends CommandRoot> HttpResponse publish(T command);
}
