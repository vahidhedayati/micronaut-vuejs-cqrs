package gateway.command.event.events;

import gateway.command.event.commands.UserSaveCommand;

import java.util.Date;

public class UserSaved extends EventRoot {

      private String username;
    private String password;

    private String firstname;

    private String surname;

    private Date lastUpdated;

    public UserSaved() {
        super();
    }
    public UserSaved(UserSaveCommand cmd) {
        super(cmd);
        this.username=cmd.getUsername();
        this.password=cmd.getPassword();
        this.firstname=cmd.getFirstname();
        this.surname=cmd.getSurname();
        this.lastUpdated=cmd.getLastUpdated();
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
