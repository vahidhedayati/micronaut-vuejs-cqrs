package hotel.write.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.write.commands.Command;

import java.util.UUID;

public class KafkaEventPublisher implements EventPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaSender kafkaSender;
    public KafkaEventPublisher(ObjectMapper objectMapper,KafkaSender kafkaSender) {
        this.objectMapper = objectMapper;
        this.kafkaSender=kafkaSender;
    }



    @Override
    public <T extends Command> void publish(String topic, T command) {

        String eventType = command.getClass().getSimpleName();

        EventEnvelope envelope = new EventEnvelope(eventType,command);
        String value = serializeEnvelope(envelope);

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
