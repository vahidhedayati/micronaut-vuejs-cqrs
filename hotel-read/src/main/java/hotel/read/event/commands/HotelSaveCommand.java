package hotel.read.event.commands;

import com.sun.istack.Nullable;
import io.micronaut.validation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Validated
public class HotelSaveCommand extends CommandRoot {


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

    public HotelSaveCommand() {
        super();
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


    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
