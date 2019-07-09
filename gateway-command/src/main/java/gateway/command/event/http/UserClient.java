package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

import java.util.Set;

/**
 * Typical Micronaut HTTP Client passing real command object to remove client host:
 */

@Client(id = "userbase-write", path = "/")
public interface UserClient {

    @Post("/")
    <T extends CommandRoot> void initialize(T cmd);
    @Post("/")
    <T extends CommandRoot> HttpResponse publish(T command);

}