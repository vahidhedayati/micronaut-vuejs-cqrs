package hotel.write.commands;

import java.io.Serializable;


public class HotelSaveCommand  extends Command  {


    private Long id;


    private String code;


    private String name;

    private String phone;

    private String email;


    public HotelSaveCommand() {}

    public HotelSaveCommand(String code, String name) {
        this.name = name;
        this.code=code;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
