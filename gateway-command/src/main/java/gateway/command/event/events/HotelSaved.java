package gateway.command.event.events;

import com.sun.istack.Nullable;
import gateway.command.event.commands.HotelSaveCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

public class HotelSaved extends EventRoot {

    @NotBlank
    @Pattern(regexp = "(?=.*[A-Z]).{2,3}", message = "field_three_char")
    private String code;

    @NotNull
    @NotBlank
    @Size(max = 10, min=3)
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;

    @Nullable
    private Long updateUserId;

    private Optional<String> updateUserName;


    public HotelSaved() {super();}

    public HotelSaved(HotelSaveCommand cmd) {
        super(cmd);
        this.name=cmd.getName();
        this.code=cmd.getCode();
        this.email=cmd.getEmail();
        this.phone=cmd.getPhone();
        this.updateUserId=cmd.getUpdateUserId();

    }

    public Optional<String> getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(Optional<String> updateUserName) {
        this.updateUserName = updateUserName;
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
}
