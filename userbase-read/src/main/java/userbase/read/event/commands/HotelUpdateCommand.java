package userbase.read.event.commands;

import io.micronaut.validation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Validated
public class HotelUpdateCommand  extends CommandRoot {

    @NotNull
    private Long id;

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


    public HotelUpdateCommand() {}
    public HotelUpdateCommand(HotelUpdateCommand cmd) {
        super((CommandRoot) cmd);
        this.name=cmd.getName();
        this.code=cmd.getCode();
        this.email=cmd.getEmail();
        this.phone=cmd.getPhone();
        this.id=cmd.getId();
    }
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
