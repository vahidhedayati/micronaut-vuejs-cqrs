package hotel.read.event.listeners;


import hotel.read.commands.Command;
import hotel.read.commands.HotelDeleteCommand;
import hotel.read.commands.HotelSaveCommand;
import hotel.read.commands.HotelUpdateCommand;
import hotel.read.services.read.HotelService;
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
            put(HotelSaveCommand.class.getSimpleName(), HotelSaveCommand.class);
            put(HotelUpdateCommand.class.getSimpleName(), HotelUpdateCommand.class);
            put(HotelDeleteCommand.class.getSimpleName(), HotelDeleteCommand.class);
        }
    };

    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;


   // @GuardedBy("kafkaConsumers")
  //  private final Set<Consumer> kafkaConsumers = new HashSet<>();

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
       // synchronized (kafkaConsumers) {
       //     this.kafkaConsumers.add(consumer);
        //}
    }

    //protected static final Logger LOG = LoggerFactory.getLogger(KafkaEventListener.class);

    @Inject
    private HotelService dao;


    @Topic("hotelRead")
    public void consume(@KafkaKey String hotelCode,  String hotelCreatedEvent) {
        System.out.println("_____> READ --------------- KAKFA "+hotelCode);
        if (hotelCode.contains("_")) {
            String eventType = hotelCode.split("_")[0];
            if (hotelCode!=null) {
                // LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
                //System.out.println("READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  hotelCreated ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
                System.out.println("_____> READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  --- "+hotelCode+ " -- event "+hotelCreatedEvent);

                JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                        .orElseThrow(() -> new IllegalStateException("No JSON codec found"));

                System.out.println(eventType+" is current eventType ");
                Command cmd = (Command) mediaTypeCodec.decode(commandClasses.get(eventType),hotelCreatedEvent);
                // System.out.println(" command "+cmd);

                System.out.println(" HOTEL-READ ALL good ---->"+cmd.getClass());
                if (cmd instanceof HotelSaveCommand) {
                    System.out.println("Saving item sending to  hotel");
                    dao.save((HotelSaveCommand) cmd);
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
