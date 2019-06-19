package hotel.write.commands;

public class HotelUpdatedCommand  extends  HotelUpdateCommand {

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    private String updateUserName;




}
