package gateway.command.init;

import gateway.command.event.commands.UserSaveCommand;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoUsersFactory {

	
	static List<UserSaveCommand> defaultUsers() {
		List<UserSaveCommand> users = new ArrayList<UserSaveCommand>();
		users.add(addUser("admin","password", "Alan", "Wollenstein"));
		users.add(addUser("susan","password", "Susan", "Jones"));
		users.add(addUser("bill","password", "Billy", "Smith"));
		users.add(addUser("ben","password", "Benjamin", "Thomas"));
        System.out.println(" users to be added "+users.size());
		return users;
    }
	
	
	static UserSaveCommand addUser(String username,String password,String firstname,String surname) {
		UserSaveCommand user = new UserSaveCommand(username,password ,firstname,surname,
				Date.from(LocalDate.parse( "2019-01-10" ).plusDays( 10 ).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		return user;
	}



	
}
