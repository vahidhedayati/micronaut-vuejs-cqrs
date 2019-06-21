package userbase.write.commands;

import java.util.Optional;

public class HotelSavedCommand extends  HotelSaveCommand {


    public HotelSavedCommand() {super();}

    public HotelSavedCommand(HotelSaveCommand cmd) {
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
