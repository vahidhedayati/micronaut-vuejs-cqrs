package hotel.read.event.events;

import hotel.read.event.commands.UserUpdateCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserUpdated extends EventRoot {
    @NotNull
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;


    @NotBlank
    private String firstname;


    @NotBlank
    private String surname;

    public UserUpdated() {
        super();
    }


    public UserUpdated(UserUpdateCommand cmd) {
        super(cmd);
        this.username = cmd.getUsername();
        this.password = cmd.getPassword();
        this.firstname = cmd.getFirstname();
        this.surname = cmd.getSurname();
        this.id = cmd.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }



}
