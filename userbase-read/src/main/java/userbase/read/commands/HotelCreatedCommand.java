package userbase.read.commands;


public class HotelCreatedCommand extends HotelCreateCommand  {

    public HotelCreatedCommand() {
        super();
    }

    public HotelCreatedCommand(HotelCreateCommand cmd) {
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
