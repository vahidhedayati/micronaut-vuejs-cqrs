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
import java.util.HashMap;
import java.util.Iterator;
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
    private ConcurrentHashMap<String, WebSocketSession> currentSubmissions = new ConcurrentHashMap<>();

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
        System.out.println("SOCKET OPENED "+session);
        sessions.put(session.getId(), session);
        //String message = String.valueOf(userRepository.count());
        String message="Hello";
        publishMessage(message, session);
    }

    /**
     * hotelForm.vue submits currentUser which is a randomly generated id on that page to this socket connector
     * when a user attempts to submit hotelForm
     * this picks up and registers the currentUser which is also now in Command.java object and is passed from the actual
     * form to this page actioned by process(String topic, @JsonProperty("eventType") String eventType, @Body String formInput)
     * action below and upon form failues - it will get the websocket session to return response to ...
     *
     * this then completes the form validation having a way back from the command bus back to user view.
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        System.out.println("SOCKET MESSage "+message);
        JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                .orElseThrow(() -> new IllegalStateException("No JSON codec found"));
        WebsocketMessage msg  = mediaTypeCodec.decode(WebsocketMessage.class, message);

        if (msg!=null) {
            //System.out.println("Found  >"+msg.getEventType()+"< EVENT -------------------------------------------------------");
            switch(msg.getEventType()) {
                case "userForm":
                    if (!currentSubmissions.contains(msg.getCurrentUser())) {
                        System.out.println("Adding user "+message+" to concurrent hashmap currentSubmissions");
                        currentSubmissions.put(msg.getCurrentUser(),session);
                    }
                    break;
                case "errorForm":
                    System.out.println("Error has happened relaying back to user "+msg.getCurrentUser()+" were errors being relayed ");
                    WebSocketSession userSession = currentSubmissions.get(msg.getCurrentUser());
                    if (userSession!=null) {
                        System.out.println("Found  "+userSession+" going to send error");
                        Iterator hmIterator = msg.getErrors().entrySet().iterator();
                        while (hmIterator.hasNext()) {
                            Map.Entry mapElement = (Map.Entry)hmIterator.next();

                            System.out.println("Error being sent"+mapElement.getValue());
                            userSession.sendAsync("{ currentUser: "+msg.getCurrentUser()+", error: "+mapElement.getValue()+"}");

                        }
                    } else {
                        System.out.println("Could not find user !!");
                    }
                    break;
            }

        }

    }



    public void publishMessage(String message, WebSocketSession session) {
        //session.broadcastSync(message);
        broadcaster.broadcast(message);
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("SOCKET error "+error);

        LOG.info("on Error");
    }

    @OnClose
    public void onClose(CloseReason closeReason, WebSocketSession session) {
        LOG.info("on Close");
        System.out.println("SOCKET close ");
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
        /**
         * We attempt to send current websocket session with command to which ever command handler
         */

        if (cmd.getCurrentUser()!=null) {
            //System.out.println(" Websocket test for  "+cmd.getCurrentUser());
            WebSocketSession session = currentSubmissions.get(cmd.getCurrentUser());
            if (session!=null) {
                //System.out.println(" Websocket session =   "+session);
                cmd.setSession(session);
                //System.out.println(" removing  session =   ");//+session);
                //currentSubmissions.remove(cmd.getCurrentUser());
            }

        } else {
            System.out.println(" No current user found  "+cmd.getCurrentUser());
        }

        System.out.println("Publishing--------- command "+cmd+ " Current user "+cmd.getCurrentUser());
        eventPublisher.publish(embeddedServer,topic,cmd);

        /**
         * We attempt to send current websocket session with command to which ever command handler
         */
/*
        if (cmd.getCurrentUser()!=null) {
            System.out.println(" Websocket test for  "+cmd.getCurrentUser());
            WebSocketSession session = currentSubmissions.get(cmd.getCurrentUser());
            if (session!=null) {
                eventPublisher.publish(session, embeddedServer,topic,cmd);
                System.out.println(" Websocket session =   "+session);
                System.out.println(" removing  session =   ");//+session);
                currentSubmissions.remove(cmd.getCurrentUser());
                System.out.println("Publishing--------- command "+cmd+ " Current user "+cmd.getCurrentUser()+"  has session"+session);

            } else {
                System.out.println("Publishing--------- command "+cmd+ " Current user "+cmd.getCurrentUser());
                eventPublisher.publish(embeddedServer,topic,cmd);
            }

        } else {
            System.out.println("Publishing--------- command "+cmd+ " Current user "+cmd.getCurrentUser());
            eventPublisher.publish(embeddedServer,topic,cmd);
        }
*/


        return HttpResponse.accepted();
    }

}
