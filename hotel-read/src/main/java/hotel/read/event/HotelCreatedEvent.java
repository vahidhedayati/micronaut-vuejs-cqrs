package hotel.read.event;

import hotel.read.adaptors.models.HotelCreatedCommand;
import hotel.read.domain.Hotel;

import java.io.Serializable;


public class HotelCreatedEvent extends AbstractEvent<HotelCreatedCommand> implements  Serializable{

    private static final long serialVersionUID = -7452000227812130114L;

    public HotelCreatedCommand getHotelCreatedCommand() {
        return hotelCreatedCommand;
    }

    public void setHotelCreatedCommand(HotelCreatedCommand hotelCreatedCommand) {
        this.hotelCreatedCommand = hotelCreatedCommand;
    }

    private HotelCreatedCommand hotelCreatedCommand;

    public HotelCreatedEvent() {

    }

    public HotelCreatedEvent(HotelCreatedCommand hotel) {
        this.hotelCreatedCommand = hotel;
    }

    /**
     * getEventId must be specific to kafka - fails due to null id
     * @return
     */
    @Override
    public String getEventId() {
        System.out.println("READ ---- getEventId: " +  getDtoFromEvent().getCode());
        return getDtoFromEvent().getCode();
        //return getDtoFromEvent().getCode().toString();
    }
    @Override
    public String getEventCode() {
        System.out.println("READ -------- getEventCode: " +  getDtoFromEvent().getCode());
        return getDtoFromEvent().getCode();
    }
    @Override
    public HotelCreatedCommand getDtoFromEvent() {
        return this.hotelCreatedCommand;
    }

}
