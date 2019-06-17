package gateway.command.event.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;

import java.io.IOException;
import java.util.Optional;

public interface MapCommand {

    static  <T extends Command> T findCommand(String command,Class clazz) {

        try {
            System.out.println(clazz+" rying to about to try : "+command);
            Optional<Object>  obj = Optional.of(new ObjectMapper().readValue(command, clazz));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  obj = "+obj.getClass());
            return ((T) obj.get());
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }

        return null;
    }
}
