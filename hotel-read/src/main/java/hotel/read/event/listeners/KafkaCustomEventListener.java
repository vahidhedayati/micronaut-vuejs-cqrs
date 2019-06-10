package hotel.read.event.listeners;

import hotel.read.domain.interfaces.Hotels;
import hotel.read.event.HotelCreatedEvent;
import hotel.read.event.HotelDeletedCommandEvent;
import hotel.read.event.HotelUpdateCommandEvent;
import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Primary;
import io.micronaut.messaging.annotation.Body;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ThreadSafe
@KafkaListener
@Primary
public class KafkaCustomEventListener implements ConsumerRebalanceListener, ConsumerAware {

    //private Consumer consumer;

    @GuardedBy("kafkaConsumers")
    private final Set<Consumer> kafkaConsumers = new HashSet<>();

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        //Validate.notNull(consumer, "Missing Kafka Consumer instance");
        //LOG.info("Got aware of a Kafka Consumer {} with subscriptions to {}", consumer, consumer.subscription());
        synchronized (kafkaConsumers) {
            this.kafkaConsumers.add(consumer);
        }
    }

    protected static final Logger LOG = LoggerFactory.getLogger(KafkaCustomEventListener.class);

    @Inject
    private Hotels dao;
    //private QueryHotelViewDao dao;

    @Topic("hotelCreated")
    public void consume( @KafkaKey String hotelCode, @Body HotelCreatedEvent hotelCreatedEvent) {
        LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
        System.out.println("READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  hotelCreated ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
        dao.save(hotelCreatedEvent.getDtoFromEvent().createHotel());
    }

    @Topic("hotelEdit")
    public void consumeEdit( @KafkaKey String hotelCode, @Body HotelUpdateCommandEvent hotelCreatedEvent) {
        LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER consumeEdit "+hotelCode);
        System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER consumeEdit ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
        dao.update(hotelCreatedEvent.getDtoFromEvent());
    }

    @Topic("hotelDelete")
    public void consumeDelete( @KafkaKey String hotelCode, @Body HotelDeletedCommandEvent hotelCreatedEvent) {
        LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelDelete "+hotelCode);
        System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelDelete ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
        dao.delete(hotelCreatedEvent.getDtoFromEvent());
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        // save offsets here
    }


    /**
     * This triggers a new node to build h2 db up based on existing received kafka events
     * @param partitions
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.println("Partitions "+partitions);
        // seek to offset here
        //consumer.seekToBeginning(consumer.assignment());
        for (TopicPartition partition : partitions) {
            kafkaConsumers.forEach(consumer -> {
                //System.out.println(" Consumer = ------------------------------>" + consumer);
                synchronized (consumer) {
                    consumer.seekToBeginning(consumer.assignment());
                }
            });
        };
    }

}
