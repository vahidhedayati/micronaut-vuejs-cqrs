package userbase.write.commands;

import java.util.Optional;

public class HotelUpdatedCommand  extends  HotelUpdateCommand {


    public HotelUpdatedCommand() {super();}

    public HotelUpdatedCommand(HotelUpdateCommand cmd) {
        super(cmd);
    }

    private Optional<String> updateUserName;

    public Optional<String> getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(Optional<String> updateUserName) {
        this.updateUserName = updateUserName;
    }
}
