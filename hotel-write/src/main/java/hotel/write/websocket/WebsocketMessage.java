package hotel.write.websocket;

import java.util.HashMap;
import java.util.Optional;

public class WebsocketMessage {

    //This is current user from vuejs - generated as part of initial socket connection
    private String currentUser;

    //This relates to what the socket is dealing with i.e. userForm  i.e. user input  ( errorForm / successForm : this is its own responses)
    //find this in gateway-command/GatewayController
    private String eventType;

    //This is the errors bound back when it did form validation in remote micro service command handler
    private HashMap<String,String> errors;

    //If the record saved we need to capture the ID and send back to user front end
    //Is optional to make it non blocking otherwise as normal id - app hangs on saving
    private Optional<Long> id;


    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }

    public Optional<Long> getId() {
        return id;
    }

    public void setId(Optional<Long> id) {
        this.id = id;
    }
}
