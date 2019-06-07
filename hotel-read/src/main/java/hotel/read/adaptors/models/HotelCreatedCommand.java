package hotel.read.adaptors.models;


import hotel.read.domain.Hotel;
import hotel.read.domain.HotelRooms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HotelCreatedCommand implements Serializable  {

    private Long id;


    private String code;


    private String name;

    private String phone;

    private String email;


    private Long updateUserId;


    private Date lastUpdated;

    private List<HotelRooms> hotelRooms;

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

    public HotelCreatedCommand(String code, String name, String phone, String email, Long updateUserId, Date lastUpdated, List<HotelRooms> hotelRooms) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.updateUserId = updateUserId;

        this.lastUpdated = lastUpdated;
        this.hotelRooms = hotelRooms;
    }
    public HotelCreatedCommand(Hotel h) {
        this.code = h.getCode();
        this.name = h.getName();
        this.phone = h.getPhone();
        this.email = h.getEmail();
        this.updateUserId = h.getUpdateUserId();
        this.lastUpdated = h.getLastUpdated();
        this.hotelRooms = h.getHotelRooms();
    }

    public Hotel createHotel() {
        return new Hotel(this.code,this.name, this.phone, this.email, this.updateUserId, this.hotelRooms, this.lastUpdated);
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
