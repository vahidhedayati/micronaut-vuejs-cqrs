package hotel.write.event.client.eventStore;

import hotel.write.domain.Hotel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class HotelDataFactory implements HotelInterfaceFactory {

    @Override
    public Data getData(String eventType, String eventCode, Hotel hotel) {
        return new Data(metaData(eventType), jsonHotel(hotel));
    }

    String metaData (String eventType) {
        return "{" +
                "timestamp='" +new DateTime(DateTimeZone.UTC).toString()+ '\'' +
                "eventType='" +eventType+ '\'' +
                '}';

    }

    String jsonHotel(Hotel hotel) {
        return "{" +
                "id=" + hotel.getId() +
                ", code='" + hotel.getCode() + '\'' +
                ", update_user_id='" + hotel.getUpdateUserId() + '\'' +
                ", phone='" + hotel.getPhone() + '\'' +
                ", email='" + hotel.getEmail() + '\'' +
                ", rooms='" + hotel.getHotelRooms() + '\'' +
                ", name='" + hotel.getName() + '\'' +
                '}';

    }
}
