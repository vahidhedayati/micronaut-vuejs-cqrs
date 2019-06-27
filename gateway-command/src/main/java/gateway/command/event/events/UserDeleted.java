package gateway.command.event.events;

import gateway.command.event.commands.UserDeleteCommand;

import javax.validation.constraints.NotNull;

public class UserDeleted extends EventRoot {


    @NotNull
    private Long id;


    public UserDeleted() {
        super();
    }


    public UserDeleted(Long id) {
        this.id = id;

    }
    public UserDeleted(UserDeleteCommand cmd) {
        super(cmd);
        this.id = cmd.getId();
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
}
