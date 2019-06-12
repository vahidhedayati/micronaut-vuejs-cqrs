package hotel.write.event.client;

import hotel.write.event.AbstractEvent;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;


/**
 * This sends kafka client message out
 *
 * @param <T>
 */

@KafkaClient
public interface EventClient<T> {

    @Topic("hotelCreated1")
    void sendEvent(@KafkaKey String hotelId, @Body AbstractEvent<T> hotelEvent);

    @Topic("hotelEdit1")
    void sendEventEdit(@KafkaKey String hotelId, @Body AbstractEvent<T> hotelEvent);

    @Topic("hotelDelete1")
    void sendEventDelete(@KafkaKey String hotelId, @Body AbstractEvent<T> hotelEvent);
}
