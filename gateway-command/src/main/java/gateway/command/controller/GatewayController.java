package gateway.command.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.command.event.commands.CommandRoot;
import gateway.command.event.http.DefaultClient;
import gateway.command.event.http.HttpEventPublisher;
import io.micronaut.discovery.exceptions.NoAvailableServiceException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@Slf4j
@ServerWebSocket("/ws/process")
@Controller("/")
public class GatewayController {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayController.class);


    private final String CLASS_PATH="gateway.command.event.commands.";
    private final String HTTP_PATH="gateway.command.event.http.";


    private final DefaultClient defaultClient;
    final EmbeddedServer embeddedServer;



    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;


    @Inject
    public GatewayController( DefaultClient defaultClient,EmbeddedServer embeddedServer) {
        this.defaultClient=defaultClient;
        this.embeddedServer=embeddedServer;
    }


    /**
     *
     * @param topic
     * @param eventType using the jsonProperty we actually extract eventType from the @Body string JSON String
     *                  The 3rd input is actual form. we post /hotel and json content there isn't actually 3 parameters
     *                  provided
     *
     *
     * @param
     * @return HttpResonse from remote end
     * This has now been made synchronous - using http events rather than kafka events which are by nature asynchronous
     * You do get RecordMetadata from kafka if you do defauly synchronous blocking call -
     * but this contains no process data from remote end.
     */
    @Post(uri = "/{topic}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse process(String topic, @JsonProperty("eventType") String eventType, @Body String formInput)  {
        JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                .orElseThrow(() -> new IllegalStateException("No JSON codec found"));
        try {
            CommandRoot cmd = (CommandRoot) mediaTypeCodec.decode( Class.forName(CLASS_PATH+eventType),formInput);
            cmd.initiate(embeddedServer,eventType);

            //String representation of http class listeners gateway.command.event.http.HotelListener or UserListener
            String httpClassName = HTTP_PATH+topic.substring(0, 1).toUpperCase() + topic.substring(1)+"Listener";

            /**
             * This grabs our dynamic mapper for above class which calls the abstract method in above class
             * physically identified under its abstract name of HttpEventPublisher so HotelListener pretending to be
             * HttpEventPublisher so we can instantiate it
             */
            HttpEventPublisher d = (HttpEventPublisher) makeObject(Class.forName(httpClassName));
            /**
             * This calls the publish method in the underlying class
             */
            try {
                return d.publish(defaultClient,cmd);
            } catch (NoAvailableServiceException exception) {
                /**
                 * When a service / aggregate root - attempt fails send an immediate error to user and fail task
                 * no queueing of the command for future replay is required here
                 */
                LOG.error("NoAvailableServiceException - adding event to Events Queue "+exception.getMessage(),exception);

                Set<String> failureMessages = new HashSet<String>();
                failureMessages.add(d.getClass().getSimpleName()+" using httpClient "+defaultClient.getClass().getSimpleName()+" service are down");
                HashMap<String,Set<String>> errors = new HashMap<>();
                errors.put("error", failureMessages);
                return HttpResponse.ok(errors);

            }
        } catch (ClassNotFoundException e) {
            LOG.error("ClassNotFoundException "+e.getMessage(),e);
        }
        //return Maybe.just(HttpResponse.serverError());
        return HttpResponse.serverError();
    }


    /**
     * Assigns and returns actual object instance of given Class object
     * @param clazz
     * @return
     */
    public Object makeObject(Class<?> clazz) {
        Object o = null;

        try {
            if (HttpEventPublisher.class.isAssignableFrom(clazz)) {
                o = Class.forName(clazz.getName()).newInstance();
            } else {

                throw new RuntimeException(
                        "Invalid class: class should be child of MyInterface");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }
}
