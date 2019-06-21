package hotel.read.commands;


import java.util.Optional;

public class HotelCreatedCommand extends HotelCreateCommand  {

    public HotelCreatedCommand() {
        super();
    }

    public HotelCreatedCommand(HotelCreateCommand cmd) {
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
