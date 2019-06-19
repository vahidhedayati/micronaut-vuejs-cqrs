package hotel.read.commands;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class UserSaveCommand extends Command {

    private String username;
    private String password;

    private String firstname;

    private String surname;

    private Date lastUpdated;

    public UserSaveCommand() {}

    public UserSaveCommand(@NotBlank String username, @NotBlank String password, @NotBlank String firstname, @NotBlank String surname) {

        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;
    }

    public UserSaveCommand(@NotBlank String username, @NotBlank String password, @NotBlank String firstname, @NotBlank String surname, @NotBlank Date lastUpdated) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;
        this.lastUpdated = lastUpdated;
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
