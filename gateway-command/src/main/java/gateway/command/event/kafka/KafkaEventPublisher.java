package gateway.command.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.commands.Command;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;
import org.apache.kafka.clients.producer.ProducerRecord;
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
        System.out.println(" About to send to kafka  topic: "+topic);

        command.setEventType(command.getClass().getSimpleName());

        /*

            //TODO - having issues extracting this on hotel-write - the actual object or command seems to work
            //TODO - this is preferred method since it contains useful information it gets stuck on trying to put together eventData
            //TODO - Which is the extended class I understand why not sure what the fix would be having tried a few things already

            EventEnvelope envelope = new EventEnvelope(embeddedServer,eventType,command);
            String value = serializeEnvelope(envelope);
            System.out.println(" About to send to kafka"+topic+" ------------"+value);

          //  Future<RecordMetadata> recordMetadataFuture = kafkaSender.send(topic, envelope.getTransactionId().toString(), value);
        Future<RecordMetadata> recordMetadataFuture = kafkaSender.send(topic,UUID.randomUUID().toString(), value);

*/

        /**
         * This is using the default extended command object generating json string and passing to who ever listens in to the
         * dynamic topic being listened to
         */
        String value1 =serializeCommand(command);
        System.out.println("  kafka topic: "+topic+" ------------ Serialized values: "+value1);
        Future<RecordMetadata> recordMetadataFuture = kafkaSender.send(topic,   command.getEventType()+"_"+UUID.randomUUID().toString(), value1);

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

    private String serializeEnvelope(EventEnvelope envelope) {
        String json;
        try {
            json = objectMapper.writeValueAsString(envelope);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
