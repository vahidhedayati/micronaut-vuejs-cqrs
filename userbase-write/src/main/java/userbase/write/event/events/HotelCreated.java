package userbase.write.event.events;


import com.sun.istack.Nullable;
import userbase.write.event.commands.HotelRoomsCreateCommand;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class HotelCreated extends EventRoot {


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
    private List<HotelRoomsCreated> hotelRooms;


    public HotelCreated() {
        super();
    }



    private Optional<String> updateUserName;

    public Optional<String> getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(Optional<String> updateUserName) {
        this.updateUserName = updateUserName;
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

    public List<HotelRoomsCreated> getHotelRooms() {
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

    public void setCode(String code) {
        this.code = code;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }



    public void setHotelRooms(List<HotelRoomsCreateCommand> hotelRooms) {
        List<HotelRoomsCreated> hotelRms = new ArrayList<>();
        for (HotelRoomsCreateCommand cmd:hotelRooms) {
            hotelRms.add(new HotelRoomsCreated(cmd));

        }
        this.hotelRooms = hotelRms;
    }
}
