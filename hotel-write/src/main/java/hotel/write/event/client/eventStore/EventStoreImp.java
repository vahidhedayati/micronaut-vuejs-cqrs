package hotel.write.event.client.eventStore;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


import eventstore.EventData;
import eventstore.Settings;
import eventstore.WriteEvents;
import eventstore.j.EventDataBuilder;
import eventstore.j.SettingsBuilder;
import eventstore.j.WriteEventsBuilder;
import eventstore.tcp.ConnectionActor;
import hotel.write.domain.Hotel;
import hotel.write.event.AbstractEvent;
import io.micronaut.retry.annotation.Recoverable;


import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.UUID;
@Recoverable
@Singleton
public class EventStoreImp implements EventStoreClient<Hotel> {

  //  @Override
//
    public void sendEvent1(AppEventData data) {
        ActorSystem system = ActorSystem.create();

        Settings settings = new SettingsBuilder()
                .address(new InetSocketAddress("localhost",2113))
                .defaultCredentials("admin", "changeit")
                .build();

        ActorRef connection = system.actorOf(ConnectionActor.getProps(settings));
        ActorRef writeResult = system.actorOf(Props.create(WriteResult.class));

        EventData event = new EventDataBuilder(data.getValue())
                .eventId(UUID.randomUUID())
                .data(data.getValue())
                .metadata(data.getMetadata())
                .build();

        WriteEvents writeEvents = new WriteEventsBuilder("hotelCreated")
                .addEvent(event)
                .expectAnyVersion()
                .build();

        connection.tell(writeEvents, writeResult);
    }

    @Override
    public void sendEvent(String eventType, String hotelId, AbstractEvent<Hotel> hotelEvent) {
        ActorSystem system = ActorSystem.create();

        Settings settings = new SettingsBuilder()
                .address(new InetSocketAddress("localhost",2113))
                .defaultCredentials("admin", "changeit")
                .build();
        System.out.println("Attempting to send Event via EventStore");
        Data data = new HotelDataFactory().getData(eventType, hotelEvent.getEventCode(), hotelEvent.getDtoFromEvent());
        ActorRef connection = system.actorOf(ConnectionActor.getProps(settings));
        ActorRef writeResult = system.actorOf(Props.create(WriteResult.class));

        EventData event = new EventDataBuilder(data.getValue())
                .eventId(UUID.randomUUID())
                .data(data.getValue())
                .metadata(data.getMetadata())
                .build();

        WriteEvents writeEvents = new WriteEventsBuilder(eventType)
                .addEvent(event)
                .expectAnyVersion()
                .build();
        System.out.println("connection.tell");
        connection.tell(writeEvents, writeResult);
        System.out.println("All done");
    }


}
