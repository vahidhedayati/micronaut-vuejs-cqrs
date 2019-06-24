package hotel.write.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.write.commands.Command;
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
    public <T extends Command> void publish(EmbeddedServer embeddedServer, String topic, T command) {
        if (command.getTransactionId() !=null) {
            String value = serializeCommand(command);
            kafkaSender.send(topic,command.getEventType()+"_"+ command.getTransactionId().toString(), value);
        }
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
