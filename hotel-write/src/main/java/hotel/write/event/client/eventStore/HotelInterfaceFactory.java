package hotel.write.event.client.eventStore;

import hotel.write.domain.Hotel;

public interface HotelInterfaceFactory {

    public AppEventData getData(String eventType,String eventCode, Hotel hotel);
}
