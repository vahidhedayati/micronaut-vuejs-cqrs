package hotel.write.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.write.event.events.EventRoot;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;

@Primary
public class KafkaEventPublisher implements EventPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaSender kafkaSender;

    public KafkaEventPublisher(ObjectMapper objectMapper,KafkaSender kafkaSender) {
        this.objectMapper = objectMapper;
        this.kafkaSender=kafkaSender;
    }



    @Override
    public <T extends EventRoot> void publish(EmbeddedServer embeddedServer, String topic, T command) {
        if (command.getTransactionId() !=null) {
            kafkaSender.send(topic,command.getEventType()+"_"+ command.getTransactionId().toString(), command);
        }
    }
}
