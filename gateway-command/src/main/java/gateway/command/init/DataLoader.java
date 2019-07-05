package gateway.command.init;

import gateway.command.controller.GatewayController;
import gateway.command.domain.Events;
import gateway.command.event.commands.HotelCreateCommand;
import gateway.command.event.commands.UserSaveCommand;
import gateway.command.event.http.DefaultClient;
import gateway.command.event.http.HotelClient;
import gateway.command.event.http.UserClient;
import gateway.command.service.GatewayService;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.discovery.exceptions.NoAvailableServiceException;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Singleton
@RequiredArgsConstructor
public class DataLoader  implements ApplicationEventListener<ServerStartupEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

	private final HotelClient hotelClient;
	private final UserClient userClient;
	private final EmbeddedServer embeddedServer;
	protected final GatewayService service;


	@Inject
	protected MediaTypeCodecRegistry mediaTypeCodecRegistry;

	public DataLoader(GatewayService service, EmbeddedServer embeddedServer, HotelClient hotelClient, UserClient userClient) {

		this.service = service;
		this.embeddedServer=embeddedServer;
		this.hotelClient = hotelClient;
		this.userClient = userClient;
	}

	@Override
	public void onApplicationEvent(ServerStartupEvent event) {

		List<UserSaveCommand> users = DemoUsersFactory.defaultUsers(embeddedServer);
		for (UserSaveCommand cmd : users) {
			try {
				userClient.publish(cmd);
			} catch (NoAvailableServiceException exception) {
				LOG.error("NoAvailableServiceException - adding event to Events Queue "+exception.getMessage(),exception);
				System.out.println(" CMD TPC ----------------------------------------------------------------"+cmd.getTopic());
				service.save(new Events(new Date(), userClient.getClass().getName(), cmd.getTopic(), cmd.getTransactionId().toString(), cmd.getEventType(),  service.serializeMessage(cmd)));
			}

		}

		List<HotelCreateCommand> hotels = DemoHotelsFactory.defaultHotels(embeddedServer);
		for (HotelCreateCommand cmd : hotels) {
			try {
				hotelClient.publish(cmd);
			} catch (NoAvailableServiceException exception) {
				LOG.error("NoAvailableServiceException - adding event to Events Queue "+exception.getMessage(),exception);
				System.out.println(" CMD TPC ------------------------------------------------------------------------- "+cmd.getTopic());
				service.save(new Events(new Date(), hotelClient.getClass().getName(), cmd.getTopic(),cmd.getTransactionId().toString(),  cmd.getEventType(),  service.serializeMessage(cmd)));
			}
		}

		/**
		 * Every 60 seconds check for unprocessed events sitting on queue
		 */

		Flowable
				.timer(60, SECONDS)
				.flatMapCompletable(i -> processEvents())
				.repeat()
				.subscribe();



	}

	/**
	 * This is the Completable object passed to Flowable above
	 * @return
	 */
	Completable processEvents() {
		RunnableEvents r = new RunnableEvents(mediaTypeCodecRegistry, embeddedServer,   service);
		return Completable
				.fromRunnable(r)
				.subscribeOn(Schedulers.io())
				.doOnError(e -> LOG.error("Stuff failed", e))
				.onErrorComplete();
	}


}
