package hotel.read.event.events;

import hotel.read.event.commands.HotelUpdateCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class HotelUpdated extends EventRoot {


    public HotelUpdated() {super();}

    public HotelUpdated(HotelUpdateCommand cmd) {
        super(cmd);
        this.name=cmd.getName();
        this.code=cmd.getCode();
        this.email=cmd.getEmail();
        this.phone=cmd.getPhone();
        this.id=cmd.getId();
    }

    private Optional<String> updateUserName;

    public Optional<String> getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(Optional<String> updateUserName) {
        this.updateUserName = updateUserName;
    }

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

}
