package userbase.write.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;
import userbase.write.commands.Command;

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
        //String eventType = command.getClass().getSimpleName();

        String value = serializeCommand(command);

        System.out.println(" About to send to kafka"+topic+" ------------"+value);
        System.out.println(" Transaction ID  ---------------------------------------------- "+command.getTransactionId());

        //kafkaSender.send(topic, command.getEventType()+"_"+command.getTransactionId().toString(), value);
        kafkaSender.send(topic, command.getEventType()+"_"+ command.getTransactionId().toString(), value);
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
