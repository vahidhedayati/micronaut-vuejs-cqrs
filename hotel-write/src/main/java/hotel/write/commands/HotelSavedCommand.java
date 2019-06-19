package hotel.write.commands;

public class HotelSavedCommand extends  HotelSaveCommand {

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    private String updateUserName;


}
