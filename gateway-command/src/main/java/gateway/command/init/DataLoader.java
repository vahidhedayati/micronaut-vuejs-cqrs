package gateway.command.init;

import gateway.command.event.commands.HotelCreateCommand;
import gateway.command.event.commands.UserSaveCommand;
import gateway.command.event.http.*;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class DataLoader  implements ApplicationEventListener<ServerStartupEvent> {

	private final HotelClient hotelClient;
	private final UserClient userClient;


	@Inject
	public DataLoader(HotelClient hotelClient, UserClient userClient) {

		this.hotelClient = hotelClient;
		this.userClient = userClient;
	}

	@Override
	public void onApplicationEvent(ServerStartupEvent event) {
		List<HotelCreateCommand> hotels = DemoHotelsFactory.defaultHotels();
		for (HotelCreateCommand cmd : hotels ) {
			hotelClient.publish(cmd);
		}
		List<UserSaveCommand> users = DemoUsersFactory.defaultUsers();
		for (UserSaveCommand cmd : users ) {
			userClient.publish(cmd);
		}
	}


}
