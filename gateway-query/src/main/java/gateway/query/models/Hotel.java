package gateway.query.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Hotel {

    private Long id;


    private String code;


    private String name;

    private String phone;

    private String email;


    private Long updateUserId;

    //Ouch username was not appearing this came to was reason still mapping to what had been set before..
    //private User updateUser;


    private String updateUserName;


    private Date lastUpdated;

    private List<HotelRooms> hotelRooms;

    public Hotel() {

    }

    public Hotel(String code, String name) {
        this.code = code;
        this.name=name;
        this.hotelRooms = new ArrayList<>();
    }

    public Hotel(String code, String name, String phone, String email) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.updateUserId = 1L;
        this.lastUpdated=new Date();
        this.hotelRooms = new ArrayList<>();
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }


    public String getCode() {
    	return this.code;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public List<HotelRooms> getHotelRooms() {
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

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public HotelRoomList rooms() {
        return new HotelRoomList(hotelRooms);
    }
    @Override
    public String toString() {
        return "hotel{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", rooms='" + getHotelRooms() + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
    
}
