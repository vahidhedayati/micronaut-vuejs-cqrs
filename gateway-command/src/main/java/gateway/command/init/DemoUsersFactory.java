package gateway.command.init;

import gateway.command.event.commands.UserSaveCommand;
import io.micronaut.runtime.server.EmbeddedServer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoUsersFactory {

	
	static List<UserSaveCommand> defaultUsers(EmbeddedServer embeddedServer) {
		List<UserSaveCommand> users = new ArrayList<UserSaveCommand>();
		users.add(addUser(embeddedServer,"admin","password", "Alan", "Wollenstein"));
		users.add(addUser(embeddedServer,"susan","password", "Susan", "Jones"));
		users.add(addUser(embeddedServer,"bill","password", "Billy", "Smith"));
		users.add(addUser(embeddedServer,"ben","password", "Benjamin", "Thomas"));
        return users;
    }
	
	
	static UserSaveCommand addUser(EmbeddedServer embeddedServer, String username,String password,String firstname,String surname) {
		UserSaveCommand user = new UserSaveCommand(username,password ,firstname,surname,
				Date.from(LocalDate.parse( "2019-01-10" ).plusDays( 10 ).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		user.initiate(embeddedServer,"UserSaveCommand");
		return user;
	}



	
}
