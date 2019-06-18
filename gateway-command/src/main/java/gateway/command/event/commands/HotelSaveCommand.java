package gateway.command.event.commands;

import com.sun.istack.Nullable;


public class HotelSaveCommand extends Command {


    @Nullable
    private Long id;

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

    public HotelSaveCommand() {
        super();
    }

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


    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
