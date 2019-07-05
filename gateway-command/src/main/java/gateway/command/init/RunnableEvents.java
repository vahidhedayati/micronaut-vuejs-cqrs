package gateway.command.init;

import gateway.command.domain.Events;
import gateway.command.event.commands.CommandRoot;
import gateway.command.event.http.DefaultClient;
import gateway.command.event.http.HttpEventPublisher;
import gateway.command.service.GatewayService;
import io.micronaut.discovery.exceptions.NoAvailableServiceException;
import io.micronaut.http.MediaType;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.runtime.server.EmbeddedServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * This runnable attempts to re-run any missing commands that were meant to have gone out to WRITE side or aggregate routes
 * of this CQRS application. Looks similar to what GatewayController is doing - except this is playing transactions off DB.
 */
public class RunnableEvents  implements Runnable {

    private final String CLASS_PATH="gateway.command.event.commands.";
    private final String HTTP_PATH="gateway.command.event.http.";

    private static final Logger LOG = LoggerFactory.getLogger(RunnableEvents.class);
    protected final GatewayService service;

    final EmbeddedServer embeddedServer;


    protected final MediaTypeCodecRegistry mediaTypeCodecRegistry;
    //@Inject
    RunnableEvents( MediaTypeCodecRegistry mediaTypeCodecRegistry, EmbeddedServer embeddedServer, GatewayService service) {
        this.mediaTypeCodecRegistry=mediaTypeCodecRegistry;
        this.embeddedServer=embeddedServer;

        this.service = service;
    }

    @Override
    public void run() {

        List<Events> eventsList = service.processMissing();

        for (Events event:eventsList) {
            JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                    .orElseThrow(() -> new IllegalStateException("No JSON codec found"));
            try {
                CommandRoot cmd = (CommandRoot) mediaTypeCodec.decode( Class.forName(CLASS_PATH+event.getEventType()),event.getCommand());
                cmd.initiate(embeddedServer,event.getEventType(), event.getTopic());

                //String representation of http class listeners gateway.command.event.http.HotelListener or UserListener
                String httpClassName = HTTP_PATH+event.getTopic().substring(0, 1).toUpperCase() + event.getTopic().substring(1)+"Listener";

                /**
                 * This grabs our dynamic mapper for above class which calls the abstract method in above class
                 * physically identified under its abstract name of HttpEventPublisher so HotelListener pretending to be
                 * HttpEventPublisher so we can instantiate it
                 */
                HttpEventPublisher d = (HttpEventPublisher) makeObject(Class.forName(httpClassName));
                /**
                 * This calls the publish method in the underlying class
                 */
                System.out.println (event.getClient()+" event topic  ="+event.getTopic()+" ETP "+event.getEventType()+" TI "+event.getTransactionId()+ " cmd "+cmd.getClass()+" dddd "+d.getClass()+" HTTP "+httpClassName);
                try {
                    //TODO - this isn't working correctly as yet -
                   // d.publish(makeObject(Class.forName(event.getClient())),cmd);
                    service.markCompleted(event);
                    LOG.info("successfully published "+event.getTransactionId());
                } catch (NoAvailableServiceException exception) {
                    LOG.error("NoAvailableServiceException - This is Events Queue will ignore request HTTP server must still be down: "+exception.getMessage());
                }
            } catch (ClassNotFoundException e) {
                LOG.error("ClassNotFoundException "+e.getMessage(),e);
            }
        }
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
