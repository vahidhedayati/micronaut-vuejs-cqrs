package gateway.command.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.command.event.ProcessEvent;
import gateway.command.event.commands.*;
import gateway.command.event.kafka.EventPublisher;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.websocket.CloseReason;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerWebSocket("/ws/process")
@Controller("/")
public class GatewayController implements ApplicationEventListener<ProcessEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayController.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private WebSocketBroadcaster broadcaster;

    final EmbeddedServer embeddedServer;


    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;


    private final EventPublisher eventPublisher;



    @Inject
    public GatewayController(EmbeddedServer embeddedServer,EventPublisher eventPublisher,WebSocketBroadcaster broadcaster) {
        this.embeddedServer=embeddedServer;
        this.eventPublisher = eventPublisher;
        this.broadcaster=broadcaster;
    }


    Map<String, Class> commandClasses = new HashMap<String,Class>() {
        {
            put(HotelSaveCommand.class.getSimpleName(), HotelSaveCommand.class);
            put(HotelUpdateCommand.class.getSimpleName(), HotelUpdateCommand.class);
            put(HotelDeleteCommand.class.getSimpleName(), HotelDeleteCommand.class);
            put(UserSaveCommand.class.getSimpleName(), UserSaveCommand.class);
            put(UserUpdateCommand.class.getSimpleName(), UserUpdateCommand.class);
        }
    };


    @Override
    public void onApplicationEvent(ProcessEvent event) {
        LOG.info("User Registered");

        LOG.info("brodcasting message to {}", sessions.size());


       // String message = String.valueOf(userRepository.count());
        String message="Hello";
        for ( String webSocketSessionId : sessions.keySet()) {
            publishMessage(message, sessions.get(webSocketSessionId));
        }
    }

    @OnOpen
    public void onOpen(WebSocketSession session) {

        LOG.info("on Open");
        sessions.put(session.getId(), session);
        //String message = String.valueOf(userRepository.count());
        String message="Hello";
        publishMessage(message, session);
    }

    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        LOG.info("on Message {}", message);
    }

    public void publishMessage(String message, WebSocketSession session) {
        //session.broadcastSync(message);
        broadcaster.broadcast(message);
    }

    @OnError
    public void onError(Throwable error) {
        LOG.info("on Error");
    }

    @OnClose
    public void onClose(CloseReason closeReason, WebSocketSession session) {
        LOG.info("on Close");
        session.remove(session.getId());
    }



    /**
     *
     * @param topic
     * @param eventType using the jsonProperty we actually extract eventType from the @Body string JSON String
     *                  The 3rd input is actual form. we post /hotel and json content there isn't actually 3 parameters
     *                  provided
     * @param
     * @return
     */
    @Post(uri = "/{topic}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse process(String topic, @JsonProperty("eventType") String eventType, @Body String formInput)  {

        JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                .orElseThrow(() -> new IllegalStateException("No JSON codec found"));

        System.out.println(" command to be rn "+eventType);
        Command cmd = (Command) mediaTypeCodec.decode(commandClasses.get(eventType),formInput);
        System.out.println(" command "+cmd);
        eventPublisher.publish(embeddedServer,topic,cmd);
        return HttpResponse.accepted();
    }

}
