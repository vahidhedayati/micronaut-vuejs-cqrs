package hotel.write.event.client;

import hotel.write.event.AbstractEvent;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;


/**
 * This sends kafka client message out
 *
 * @param <T>
 */

@KafkaClient
public interface EventClient<T> {

  @Topic("hotelRead")
  void sendEvent( String hotelId, AbstractEvent<T> hotelEvent);

}

