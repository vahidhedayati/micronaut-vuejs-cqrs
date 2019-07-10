package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;

import javax.inject.Inject;

public class UserListener extends HttpEventPublisher {

    @Inject
    private  HotelClient client;

    public UserListener() {
    }

    @Override
    public <T extends CommandRoot> HttpResponse publish( T command) {
        return client.publish(command);
    }
}
