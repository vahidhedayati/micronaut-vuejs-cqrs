package gateway.command.event.commands;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HotelCreatedCommand extends Command  {



    private String code;


    private String name;

    private String phone;

    private String email;


    private Long updateUserId;


    private Date lastUpdated;

    private List<HotelRoomsCreateCommand> hotelRooms;

    public HotelCreatedCommand() {

    }

    public HotelCreatedCommand(String code, String name) {
        this.code = code;
        this.name=name;
        this.hotelRooms = new ArrayList<>();
    }

    public HotelCreatedCommand(String code, String name, String phone, String email) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.updateUserId = 1L;
        this.lastUpdated=new Date();
        this.hotelRooms = new ArrayList<>();
    }

    public HotelCreatedCommand(String code, String name, String phone, String email, Long updateUserId,  Date lastUpdated, List<HotelRoomsCreateCommand> hotelRooms) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.updateUserId = updateUserId;

        this.lastUpdated = lastUpdated;
        this.hotelRooms = hotelRooms;
    }



    public Date getLastUpdated() {
        return this.lastUpdated;
    }


    public String getCode() {
    	return this.code;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public List<HotelRoomsCreateCommand> getHotelRooms() {
        return hotelRooms;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

}
