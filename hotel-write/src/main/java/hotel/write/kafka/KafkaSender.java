package hotel.write.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import javax.inject.Singleton;
import java.util.concurrent.Future;

@Singleton
public class KafkaSender<T> {

    private final Producer<String, T> kafkaProducer;

    public KafkaSender(
            @KafkaClient Producer<String, T> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public Future<RecordMetadata> send(String topic, String transactionId, T event) {
        return kafkaProducer.send(new ProducerRecord<>(topic, transactionId, event));
    }
}
