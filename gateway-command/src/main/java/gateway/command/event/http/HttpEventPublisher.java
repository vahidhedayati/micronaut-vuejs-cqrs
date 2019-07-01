package gateway.command.event.http;

import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;
import io.reactivex.Maybe;

/**
 * This is a generic abstract class extended to be used by gateway to be able to call any extended classes of this
 * and trigger its publish method
 */


public abstract class HttpEventPublisher {


    public HttpEventPublisher() { }

    public abstract<T extends CommandRoot> Maybe<HttpResponse> publish(DefaultClient clnt, T command);

}
