package gateway.command.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.commands.*;
import gateway.command.event.kafka.EventEnvelope;
import gateway.command.event.kafka.KafkaEventPublisher;
import gateway.command.event.kafka.KafkaSender;
import gateway.command.serialize.JsonDeserializer;
import gateway.command.serialize.JsonSerializer;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.codec.CodecConfiguration;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.http.server.netty.jackson.JsonViewMediaTypeCodecFactory;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.runtime.ApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Inject;

@Slf4j
@Controller("/")
public class GatewayController  {
    //ObjectMapper viewMapper = objectMapper.copy();

    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;
    //@Inject
  //private KafkaEventPublisher eventPublisher;
   //@Inject
    //private final ObjectMapper objectMapper;
   // public GatewayController(ObjectMapper objectMapper) {
       // this.objectMapper = objectMapper;
   //}
   //private final ObjectMapper objectMapper;

    /**
     *
     * @param topic
     * @param eventType
     * @param
     * @return
     */
    @Post(uri = "/{topic}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse process(String topic, @JsonProperty("eventType") String eventType, @Body String formInput)  {

        /*
        Map<String,Command> commands = new HashMap<String,Command>() {
            {
                put(HotelSaveCommand.class.getSimpleName(), new HotelSaveCommand());
                put(HotelUpdateCommand.class.getSimpleName(), new HotelUpdateCommand());
                put(HotelDeleteCommand.class.getSimpleName(), new HotelDeleteCommand());
            }
        };
        */

        System.out.println("--------------------------------------------------------------------"+eventType);
       // try {
       //     Object  obj = objectMapper.readValue(formInput, HotelSaveCommand.class);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
      //  }
        //return json;

        //Command command = commands.get(eventType);
        //command.
        JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                .orElseThrow(() -> new IllegalStateException("No JSON codec found"));

      //  Object obj = objectMapper.readValue(formInput,HotelSaveCommand.class);
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  obj = "+obj.getClass());
        HotelSaveCommand command =  mediaTypeCodec.decode(HotelSaveCommand.class,formInput); //MapCommand.findCommand(formInput,HotelSaveCommand.class);
        if (command!=null) {
            System.out.println("22222 --------------------------------------------------------------------" + eventType);

            System.out.println(command + " is name --------------------------------- ---------------------------" + command.getClass());
        }
        //eventPublisher.publish(topic,);
        return HttpResponse.accepted();
    }




}
