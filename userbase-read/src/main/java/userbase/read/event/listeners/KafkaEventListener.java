package userbase.read.event.listeners;


import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import userbase.read.event.events.EventRoot;
import userbase.read.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;

@ThreadSafe
@KafkaListener
public class KafkaEventListener implements ConsumerRebalanceListener, ConsumerAware {

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
    }

    @Inject
    private UserService dao;

    @Topic("userRead")
    public <T extends EventRoot> void consume(@KafkaKey String hotelCode,  T cmd) {
        if (cmd!=null) {
            dao.handleEvent(cmd);
        }
    }


    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        //for(TopicPartition partition: partitions) {
        //}
    }


    /**
     * This triggers a new node to build h2 db up based on existing received kafka events
     * @param partitions
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition partition : partitions) {
            synchronized (consumer) {
                this.consumer.subscribe(Arrays.asList(partition.topic()));
            }
            ConsumerRecords<String, String> records = this.consumer.poll(100);
            try {
                this.consumer.seek(partition,1);
            } catch (Exception e) {
                rewind(records);
            }
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

        }
    }
    private void rewind(ConsumerRecords<String, String> records) {
        records.partitions().forEach(partition -> {
            long offset = records.records(partition).get(0).offset();
            consumer.seek(partition, offset);
        });
    }

}
