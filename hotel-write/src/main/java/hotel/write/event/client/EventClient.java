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

    @Topic("hotels")
    void sendEvent(@KafkaKey String hotelId, @Body AbstractEvent<T> hotelEvent);

    @Topic("hotel")
    void sendCode(@KafkaKey String hotelCode, @Body AbstractEvent<T> hotelEvent);
}
