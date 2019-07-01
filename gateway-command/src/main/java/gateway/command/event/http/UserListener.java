package gateway.command.event.http;


import gateway.command.event.commands.CommandRoot;
import io.micronaut.http.HttpResponse;
import io.reactivex.Maybe;

public class UserListener extends HttpEventPublisher {

    /**
     * This constructor is essential In gatewayController when : Class.forName(clazz.getName()).newInstance()
     * is called it needs a default constructor fall back on
     */
    public UserListener() {
    }


    /**
     * Rather long winded but this now ties in to proper micronaut http client interface and does
     * its magic to send object over to remote receiving hotel-write application any active one via consul
     * @param clnt
     * @param command
     * @return HttpResponse from remote end
     */
    @Override
    public <T extends CommandRoot> Maybe<HttpResponse> publish(DefaultClient clnt, T command) {
        System.out.println(" "+command.getClass()+" "+clnt.getClass()+" --- user listener");
        return ((UserClient)clnt).publish(command);
    }


}
