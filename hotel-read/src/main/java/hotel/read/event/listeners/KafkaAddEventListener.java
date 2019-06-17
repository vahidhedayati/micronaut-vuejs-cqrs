package hotel.read.event.listeners;

import hotel.read.adaptors.models.HotelCreatedCommand;
import hotel.read.domain.Hotel;
import hotel.read.domain.interfaces.Hotels;
import hotel.read.event.HotelCreatedEvent;
import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@KafkaListener
public class KafkaAddEventListener implements ConsumerRebalanceListener, ConsumerAware {



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

    //protected static final Logger LOG = LoggerFactory.getLogger(KafkaAddEventListener.class);

    @Inject
    private Hotels dao;
    //private QueryHotelViewDao dao;

    @Topic("hotel")
    public void consume( @KafkaKey String hotelCode, @Body HotelCreatedEvent hotelCreatedEvent) {
        if (hotelCode!=null) {
            // LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
            //System.out.println("READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  hotelCreated ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
            System.out.println("READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  hotelCreated --- "+hotelCode);
            HotelCreatedCommand cmd =  hotelCreatedEvent.getDtoFromEvent();
            if (cmd !=null ) {
                Hotel h= cmd.createHotel();
                if (h !=null ) {
                    dao.save(h);
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
                // kafkaConsumers.forEach(consumer -> {
                //synchronized (kafkaConsumers) {
                System.out.println("  onPartitionsRevoked parition : " + partition + ' ');
                // + consumer.position(partition));
                //consumer.seek(partition,1);
                // }
                // });
            }
            //   saveOffsetInExternalStore(consumer.position(partition));
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
