package hotel.write.kafka;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.write.commands.Command;
import hotel.write.commands.HotelDeleteCommand;
import hotel.write.commands.HotelSaveCommand;
import hotel.write.commands.HotelUpdateCommand;
import io.micronaut.http.MediaType;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})
public class EventEnvelope { //extends Command {

    //EmbeddedServer embeddedServer ;//= ApplicationContext.run(EmbeddedServer.class);
   // @JsonIgnore
   // private static ObjectMapper objectMapper = new ObjectMapper();


    //@JsonIgnore
   // @Inject
   // protected MediaTypeCodecRegistry mediaTypeCodecRegistry;


    @JsonProperty("eventType")
    String eventType;


    //Stores time of event
    @JsonProperty("instant")
    Instant instant;
    //Stores a random transaction Id
   // @JsonProperty("transactionId")
   // UUID transactionId;

    //Stores current hostname/port - for other useful stuff in future perhaps websocket connect back to this host
    @JsonProperty("host")
    String host;

    @JsonProperty("port")
    int port;

    //Command eventData;
    @JsonProperty("eventData")
    String eventData;


    /*
    @JsonIgnore
    public EventEnvelope(final Instant instant,final UUID transactionId, final String host, final int port) {
        Objects.requireNonNull(instant);
        this.instant = instant;
        this.transactionId=transactionId;
        this.host=host;
        this.port=port;
    }


    @JsonIgnore
    public EventEnvelope(EmbeddedServer embeddedServer, String eventType,Command eventData) {
        // this.embeddedServer=embeddedServer;
        this.eventData=eventData;
        this.eventType=eventType;
        instant = Instant.now();
        transactionId=UUID.randomUUID();
        host = embeddedServer.getHost();
        port = embeddedServer.getPort();
    }

    @JsonIgnore
    public EventEnvelope(String eventType, Instant instant, UUID transactionId, String host, int port, Command eventData) {
        this.eventType = eventType;
        this.instant = instant;
        this.transactionId = transactionId;
        this.host = host;
        this.port = port;
        this.eventData = eventData;
    }

    */

    @JsonAnyGetter
    public String getEventType() {
        return eventType;
    }

    @JsonAnySetter
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }



    @JsonAnyGetter
    public Instant getInstant() {
        return instant;
    }

    @JsonAnySetter
    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    /*
    @JsonAnyGetter
    public UUID getTransactionId() {
        return transactionId;
    }

    @JsonAnySetter
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

*/
    @JsonAnyGetter
    public String getHost() {
        return host;
    }

    @JsonAnySetter
    public void setHost(String host) {
        this.host = host;
    }

    @JsonAnyGetter
    public int getPort() {
        return port;
    }

    @JsonAnySetter
    public void setPort(int port) {
        this.port = port;
    }

    @JsonAnyGetter
    public String getEventData() {
        return eventData;
    }

    @JsonAnySetter
    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    /*
    @JsonAnySetter
    public void setEventData(String eventData) {
        JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                .orElseThrow(() -> new IllegalStateException("No JSON codec found"));

       this.eventData= (Command) mediaTypeCodec.decode(commandClasses.get(eventType),eventData);

        // System.out.println(" command "+cmd);

        //eventPublisher1.publish(embeddedServer,topic,cmd);
        //Hotel hotel = args.getHotel();
        //hotelWriteClient.save(hotel);

        //EventEnvelope cmd =  hotelCreatedEvent.getDtoFromEvent();
        System.out.println("in setter of eventEvenlope -----------------"+eventData+" ENVELOPE "+this.eventData);
    }
    */
}
