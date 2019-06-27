package hotel.read.event.commands;


import com.sun.istack.Nullable;

import java.util.Date;
import java.util.List;


public class HotelCreateCommand extends CommandRoot {


    @Nullable
    private String code;

    @Nullable
    private String name;
    @Nullable
    private String phone;
    @Nullable
    private String email;

    @Nullable
    private Long updateUserId;

    @Nullable
    private Date lastUpdated;

    @Nullable
    private List<HotelRoomsCreateCommand> hotelRooms;

    public HotelCreateCommand() {

    }

    public HotelCreateCommand(String code, String name, String phone, String email, Long updateUserId, Date lastUpdated, List<HotelRoomsCreateCommand> hotelRooms) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.updateUserId = updateUserId;

        this.lastUpdated = lastUpdated;
        this.hotelRooms = hotelRooms;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<HotelRoomsCreateCommand> getHotelRooms() {
        return hotelRooms;
    }

    public void setHotelRooms(List<HotelRoomsCreateCommand> hotelRooms) {
        this.hotelRooms = hotelRooms;
    }
}
