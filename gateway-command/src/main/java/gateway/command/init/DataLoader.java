package gateway.command.init;

import gateway.command.event.commands.HotelCreateCommand;
import gateway.command.event.commands.UserSaveCommand;
import gateway.command.event.kafka.EventPublisher;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class DataLoader  implements ApplicationEventListener<ServerStartupEvent> {

	private final EventPublisher eventPublisher;

	private final EmbeddedServer embeddedServer;

	public DataLoader(EventPublisher eventPublisher, EmbeddedServer embeddedServer) {
		this.eventPublisher = eventPublisher;
		this.embeddedServer = embeddedServer;
	}

	@Override
	public void onApplicationEvent(ServerStartupEvent event) {
		List<HotelCreateCommand> hotels = DemoHotelsFactory.defaultHotels();
		for (HotelCreateCommand cmd : hotels ) {
			eventPublisher.publish(embeddedServer,"hotel",cmd);
		}
		List<UserSaveCommand> users = DemoUsersFactory.defaultUsers();
		for (UserSaveCommand cmd : users ) {
			eventPublisher.publish(embeddedServer,"user",cmd);
		}
	}


}
