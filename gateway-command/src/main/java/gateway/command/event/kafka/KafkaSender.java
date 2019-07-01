package gateway.command.event.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import javax.inject.Singleton;
import java.util.concurrent.Future;

@Singleton
public class KafkaSender<T> {

    private final Producer<String, T> kafkaProducer;

    public  KafkaSender(
            @KafkaClient Producer<String, T > kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public Future<RecordMetadata> send(String topic, String transactionId, T event) {
        ProducerRecord record = new ProducerRecord<>(topic, transactionId, event);
        return kafkaProducer.send(record);
    }

    public void flush() {
        kafkaProducer.flush();
    }
}
