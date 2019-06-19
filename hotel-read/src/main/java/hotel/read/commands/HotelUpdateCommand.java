package hotel.read.commands;

import hotel.read.domain.Hotel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class HotelUpdateCommand  extends Command  {

    @NotNull
    private Long id;

    @NotBlank
    private String name;


    @NotBlank
    private String code;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;


    public HotelUpdateCommand() {}

    public HotelUpdateCommand(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code=code;
    }

    public HotelUpdateCommand(@NotNull Long id, @NotBlank String name, @NotBlank String code, @NotBlank String phone, @NotBlank String email) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.phone = phone;
        this.email = email;
    }

    public HotelUpdateCommand(Hotel h) {
        this.id = h.getId();
        this.name = h.getName();
        this.code = h.getCode();
        this.name = h.getName();
        this.phone = h.getPhone();
        this.email = h.getEmail();
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String name) {
        this.code = name;
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

    public Hotel createHotel() {
        return new Hotel(this.id,this.code,this.name, this.phone, this.email);
    }
}
