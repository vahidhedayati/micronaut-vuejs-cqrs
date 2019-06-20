package hotel.read.commands;

public class HotelSavedCommand extends  HotelSaveCommand {


    public HotelSavedCommand() {super();}

    public HotelSavedCommand(HotelSaveCommand cmd) {
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
