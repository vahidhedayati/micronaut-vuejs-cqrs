package gateway.command.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.commands.Command;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;
import org.apache.kafka.clients.producer.RecordMetadata;

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
        command.initiate(embeddedServer,command.getClass().getSimpleName());
        String value =serializeCommand(command);
        Future<RecordMetadata> recordMetadataFuture = kafkaSender.send(topic,  command.getEventType()+"_"+ command.getTransactionId().toString(), value);
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
