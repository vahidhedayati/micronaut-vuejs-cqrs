package gateway.command.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.commands.Command;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.UUID;
import java.util.concurrent.Future;

@Primary
public class KafkaEventPublisher implements EventPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaSender kafkaSender;
    public KafkaEventPublisher(ObjectMapper objectMapper,KafkaSender kafkaSender) {
        this.objectMapper = objectMapper;
        this.kafkaSender=kafkaSender;
    }



    @Override
    public <T extends Command> void publish(EmbeddedServer embeddedServer, String topic, T command) {
        /**
         * This sets up some default context of command bean including timestap - uuid -
         * current host / port creating command object
         */
        command.initiate(embeddedServer,command.getClass().getSimpleName());

        /**
         * This is using the default extended command object generating json string and passing to who ever listens in to the
         * dynamic topic being listened to
         */
        String value =serializeCommand(command);
        System.out.println("  kafka topic: "+topic+" ------------ Serialized values: "+value);
        Future<RecordMetadata> recordMetadataFuture = kafkaSender.send(topic,  command.getEventType()+"_"+ command.getTransactionId().toString(), value);

        System.out.println(" "+recordMetadataFuture.toString());
    }

    @Override
    public <T extends Command> String serializeCommand( T command) {
        String json;
        try {
            json = objectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

}
