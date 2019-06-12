package hotel.write.event.client;

import hotel.write.event.AbstractEvent;


/**
 * This sends kafka client message out
 *
 * @param <T>
 */

//@KafkaClient
public interface EventClient<T> {

  //  @Topic("hotelCreated1")
    //void sendEvent(@KafkaKey String hotelId, @Body AbstractEvent<T> hotelEvent);
  void sendEvent( String hotelId, AbstractEvent<T> hotelEvent);

    //@Topic("hotelEdit1")
    //void sendEventEdit(@KafkaKey String hotelId, @Body AbstractEvent<T> hotelEvent);
    void sendEventEdit(String hotelId, AbstractEvent<T> hotelEvent);
    //@Topic("hotelDelete1")
    //void sendEventDelete(@KafkaKey String hotelId, @Body AbstractEvent<T> hotelEvent);
    void sendEventDelete(String hotelId, AbstractEvent<T> hotelEvent);
}

