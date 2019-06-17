package gateway.command.event.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import javax.inject.Singleton;
import java.util.concurrent.Future;

@Singleton
public class KafkaSender {

    private final Producer<String, String> kafkaProducer;

    public KafkaSender(
            @KafkaClient Producer<String, String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public Future<RecordMetadata> send(String topic, String transactionId, String event) {
        return kafkaProducer.send(new ProducerRecord<>(topic, transactionId, event));
    }
}
