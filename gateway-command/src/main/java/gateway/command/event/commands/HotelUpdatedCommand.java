package gateway.command.event.commands;

public class HotelUpdatedCommand  extends  HotelUpdateCommand {


    public HotelUpdatedCommand() {super();}

    public HotelUpdatedCommand(HotelUpdateCommand cmd) {
        super(cmd);
    }

    private String updateUserName;

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

}
