package hotel.write.event.commands;

import javax.validation.constraints.NotNull;

public class UserDeleteCommand extends CommandRoot {

    @NotNull
    private Long id;


    public UserDeleteCommand() {}


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
