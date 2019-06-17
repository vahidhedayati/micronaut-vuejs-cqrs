package gateway.command.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.commands.Command;
import io.micronaut.context.annotation.Primary;
import org.apache.kafka.clients.producer.ProducerRecord;

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
    public <T extends Command> void publish(String topic, T command) {
        System.out.println(" About to send to kafka"+topic);
        String eventType = command.getClass().getSimpleName();

        EventEnvelope envelope = new EventEnvelope(eventType,command);
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
