package hotel.read.commands;

import javax.validation.constraints.NotNull;

public class UserDeleteCommand extends Command {

    @NotNull
    private Long id;


    public UserDeleteCommand() {}

    public UserDeleteCommand(UserDeleteCommand cmd) {
        super((Command) cmd);
        this.id = cmd.getId();
    }

    public UserDeleteCommand(Long id) {
        this.id = id;

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
