package gateway.command.init;

import gateway.command.event.commands.HotelCreateCommand;
import gateway.command.event.commands.UserSaveCommand;
import gateway.command.event.http.HotelClient;
import gateway.command.event.http.UserClient;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class DataLoader  implements ApplicationEventListener<ServerStartupEvent> {


	private final HotelClient hotelClient;
	private final UserClient userClient;
	private final EmbeddedServer embeddedServer;

	public DataLoader(EmbeddedServer embeddedServer,HotelClient hotelClient, UserClient userClient) {
		this.embeddedServer=embeddedServer;
		this.hotelClient = hotelClient;
		this.userClient = userClient;
	}

	@Override
	public void onApplicationEvent(ServerStartupEvent event) {
		List<UserSaveCommand> users = DemoUsersFactory.defaultUsers(embeddedServer);
		for (UserSaveCommand cmd : users) {
			userClient.publish(cmd).blockingGet();
		}

		List<HotelCreateCommand> hotels = DemoHotelsFactory.defaultHotels(embeddedServer);
		for (HotelCreateCommand cmd : hotels) {
			hotelClient.publish(cmd).blockingGet();

		}
	}

}
