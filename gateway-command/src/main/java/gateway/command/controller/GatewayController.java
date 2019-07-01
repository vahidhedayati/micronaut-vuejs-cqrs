package gateway.command.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.command.event.ProcessEvent;
import gateway.command.event.commands.*;
import gateway.command.event.kafka.EventPublisher;
import gateway.command.websocket.WebsocketMessage;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerWebSocket("/ws/process")
@Controller("/")
public class GatewayController implements ApplicationEventListener<ProcessEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayController.class);
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final String CLASS_PATH="gateway.command.event.commands.";
    private WebSocketBroadcaster broadcaster;
    private final EventPublisher eventPublisher;
    final EmbeddedServer embeddedServer;
    private ConcurrentHashMap<String, WebSocketSession> currentSubmissions = new ConcurrentHashMap<>();
    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;


    @Inject
    public GatewayController(EmbeddedServer embeddedServer,EventPublisher eventPublisher,WebSocketBroadcaster broadcaster) {
        this.embeddedServer=embeddedServer;
        this.eventPublisher = eventPublisher;
        this.broadcaster=broadcaster;
    }


    @Override
    public void onApplicationEvent(ProcessEvent event) {
        LOG.info("brodcasting message to {}", sessions.size());
    }

    @OnOpen
    public void onOpen(WebSocketSession session) {
        LOG.info("on Open");
        sessions.put(session.getId(), session);
    }

    /**
     * hotelForm.vue submits currentUser which is a randomly generated id on that page to this socket connector
     * when a user attempts to submit hotelForm
     * this picks up and registers the currentUser which is also now in CommandRoot.java object and is passed from the actual
     * form to this page actioned by process(String topic, @JsonProperty("eventType") String eventType, @Body String formInput)
     * action below and upon form failues - it will get the websocket session to return response to ...
     *
     * this then completes the form validation having a way back from the command bus back to user view.
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                .orElseThrow(() -> new IllegalStateException("No JSON codec found"));
        WebsocketMessage msg  = mediaTypeCodec.decode(WebsocketMessage.class, message);
        WebSocketSession userSession;
        if (msg!=null) {
            try {
                switch(msg.getEventType()) {
                    case "userForm":
                        if (!currentSubmissions.contains(msg.getCurrentUser())) {
                            currentSubmissions.put(msg.getCurrentUser(),session);
                        }
                        break;
                    case "errorForm":
                        userSession = currentSubmissions.get(msg.getCurrentUser());
                        if (userSession!=null) {
                            Iterator hmIterator = msg.getErrors().entrySet().iterator();
                            List<String> errors =new ArrayList<>();
                            while (hmIterator.hasNext()) {
                                Map.Entry mapElement = (Map.Entry)hmIterator.next();
                                errors.add("\""+mapElement.getValue()+"\"");
                            }
                            userSession.sendAsync("{ \"currentUser\" : \""+msg.getCurrentUser()+"\",  \"status\" : \"error\" ,\"errors\" : ["+ String.join(", ", errors)+"] }");
                        }
                        break;
                    case "successForm":
                        userSession = currentSubmissions.get(msg.getCurrentUser());
                        if (userSession!=null) {
                            userSession.sendAsync("{ \"currentUser\" : \""+msg.getCurrentUser()+"\", \"id\" : \""+msg.getId()+"\", \"status\": \"success\"}");
                        }
                        break;
                }
            } catch (ConcurrentModificationException e) {

            }
        }
    }



    public void publishMessage(String message, WebSocketSession session) {
        broadcaster.broadcast(message);
    }

    @OnError
    public void onError(Throwable error) {
        LOG.info("on Error");
    }

    @OnClose
    public void onClose(CloseReason closeReason, WebSocketSession session) {
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
        try {
            CommandRoot cmd = (CommandRoot) mediaTypeCodec.decode( Class.forName(CLASS_PATH+eventType),formInput);
            eventPublisher.publish(embeddedServer,topic,cmd);
        } catch (Exception e) {
            LOG.error("Class conversion issue "+e.getMessage(),e);
        }
        return HttpResponse.accepted();
    }

}
