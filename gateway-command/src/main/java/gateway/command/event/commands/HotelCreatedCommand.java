package gateway.command.event.commands;


public class HotelCreatedCommand extends HotelCreateCommand  {

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    private String updateUserName;


}
