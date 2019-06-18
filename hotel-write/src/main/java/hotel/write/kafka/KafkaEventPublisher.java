package hotel.write.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.write.commands.Command;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;

import java.util.UUID;

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
        System.out.println(" About to send to kafka"+topic);
        String eventType = command.getClass().getSimpleName();

        //EventEnvelope envelope = new EventEnvelope(embeddedServer,eventType,command);
        EventEnvelope envelope = new EventEnvelope();
        String value = serializeEnvelope(envelope);

        System.out.println(" About to send to kafka"+topic+" ------------"+value);

        kafkaSender.send(topic, UUID.randomUUID().toString(), value);
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
