package hotel.read.commands;

import hotel.read.domain.Hotel;

import javax.validation.constraints.NotNull;

public class HotelDeleteCommand  extends Command  {

    @NotNull
    private Long id;


    public HotelDeleteCommand() {}

    public HotelDeleteCommand(Long id) {
        this.id = id;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setId(String id) {
        this.id = Long.valueOf(id);
    }

    public Hotel getHotel() {
        return new Hotel(this.id);
    }

}
