package userbase.read.listeners;


import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.http.MediaType;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import userbase.read.commands.*;
import userbase.read.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@ThreadSafe
@KafkaListener
public class KafkaEventListener implements ConsumerRebalanceListener, ConsumerAware {

    Map<String, Class> commandClasses = new HashMap<String,Class>() {
        {

            put(UserSavedCommand.class.getSimpleName(), UserSavedCommand.class);
            put(UserUpdatedCommand.class.getSimpleName(), UserUpdatedCommand.class);
        }
    };
    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
    }

    @Inject
    private UserService dao;

    @Topic("userRead")
    public void consume(@KafkaKey String hotelCode,  String hotelCreatedEvent) {
        if (hotelCode!=null &&  hotelCode.contains("_")) {
            String eventType = hotelCode.split("_")[0];
            if (eventType!=null) {
                // LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
                System.out.println("_____> USER_READ  EVENT: "+eventType+"--------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  --- "+hotelCode+ " -- event "+hotelCreatedEvent);
                JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                        .orElseThrow(() -> new IllegalStateException("No JSON codec found"));

                Command cmd = (Command) mediaTypeCodec.decode(commandClasses.get(eventType),hotelCreatedEvent);
                if (hotelCreatedEvent !=null ) {
                    if (cmd instanceof UserSavedCommand) {
                        dao.save((UserSavedCommand) cmd);
                    } else if (cmd instanceof UserUpdatedCommand) {
                        dao.update((UserUpdatedCommand) cmd);
                    } else if (cmd instanceof UserDeletedCommand) {
                        dao.delete((UserDeletedCommand) cmd);
                    }
                }
            }
        }
    }


    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("onPartitionsRevoked------------------------------------------------------------------------------------------------");
        // partitions.iterator().forEachRemaining();
        // save offsets here
        for(TopicPartition partition: partitions) {
            synchronized (partition) {
                System.out.println("  onPartitionsRevoked parition : " + partition + ' ');
                // + consumer.position(partition));
                //consumer.seek(partition,1);
            }
        }
    }


    /**
     * This triggers a new node to build h2 db up based on existing received kafka events
     * @param partitions
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition partition : partitions) {
            System.out.println("onPartitionsAssigned  Topic " + partition.topic() + " polling");
            synchronized (consumer) {
                //this.consumer.
                this.consumer.subscribe(Arrays.asList(partition.topic()));
            }
            ConsumerRecords<String, String> records = this.consumer.poll(100);
            try {
                System.out.println(" Topic " + partition.topic() + " seekBegin");
                this.consumer.seek(partition,1);
            } catch (Exception e) {
                rewind(records);
                //Thread.sleep(100);
            }
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

            System.out.println("Topics done - subscribing: ");
        }
    }
    private void rewind(ConsumerRecords<String, String> records) {
        records.partitions().forEach(partition -> {
            long offset = records.records(partition).get(0).offset();
            consumer.seek(partition, offset);
        });
    }

}
