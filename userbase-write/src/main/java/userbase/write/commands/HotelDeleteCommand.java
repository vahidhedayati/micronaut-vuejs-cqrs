package userbase.write.commands;

import javax.validation.constraints.NotNull;

public class HotelDeleteCommand  extends Command  {

    @NotNull
    private Long id;


    public HotelDeleteCommand() {}

    public HotelDeleteCommand(Long id) {
        this.id = id;

    }
    public HotelDeleteCommand(HotelDeleteCommand cmd) {
        super((Command) cmd);
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
