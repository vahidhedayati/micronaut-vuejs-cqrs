package hotel.write.event.client.eventStore;

import hotel.write.event.AbstractEvent;



public interface EventStoreClient<T>   {

    //void sendEvent(AppEventData event);
    void sendEvent( String eventType, String hotelId, AbstractEvent<T> hotelEvent);

}



