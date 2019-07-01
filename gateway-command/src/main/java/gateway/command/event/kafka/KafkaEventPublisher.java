package gateway.command.event.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.commands.CommandRoot;
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
    public <T extends CommandRoot> void publish(EmbeddedServer embeddedServer, String topic, T command) {
        command.initiate(embeddedServer,command.getClass().getSimpleName());
        Future<RecordMetadata> recordMetadataFuture = kafkaSender.send(topic,  command.getEventType()+"_"+ command.getTransactionId().toString(), command);
        /*
        kafkaSender.flush();
        try {
           RecordMetadata metadata =  recordMetadataFuture.get(10, TimeUnit.SECONDS);
           //System.out.println(" Metat "+metadata.topic()+" ::::: "+metadata.serializedValueSize()+" ");
        } catch (Exception e) {

        }
        */
    }

}
