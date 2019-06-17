package gateway.command.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.command.event.commands.Command;
import gateway.command.event.commands.HotelDeleteCommand;
import gateway.command.event.commands.HotelSaveCommand;
import gateway.command.event.commands.HotelUpdateCommand;
import gateway.command.event.kafka.EventPublisher;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Controller("/")
public class GatewayController  {

    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;

    @Inject
    protected EventPublisher eventPublisher;


    private final EventPublisher eventPublisher1;

    public GatewayController(EventPublisher eventPublisher1) {
        this.eventPublisher1 = eventPublisher1;
    }

    Map<String, Class> commandClasses = new HashMap<String,Class>() {
        {
            put(HotelSaveCommand.class.getSimpleName(), HotelSaveCommand.class);
            put(HotelUpdateCommand.class.getSimpleName(), HotelUpdateCommand.class);
            put(HotelDeleteCommand.class.getSimpleName(), HotelDeleteCommand.class);
        }
    };

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

        System.out.println(" command to be rn ");
        Command cmd = (Command) mediaTypeCodec.decode(commandClasses.get(eventType),formInput);
        System.out.println(" command "+cmd);
        eventPublisher1.publish(topic,cmd);
        return HttpResponse.accepted();
    }

}
