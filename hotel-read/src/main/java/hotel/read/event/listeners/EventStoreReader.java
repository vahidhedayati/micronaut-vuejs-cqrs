package hotel.read.event.listeners;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.io.TcpConnection;
import eventstore.ReadEvent;
import eventstore.Settings;
import eventstore.j.ReadEventBuilder;
import eventstore.j.SettingsBuilder;
import eventstore.tcp.ConnectionActor;

import java.net.InetSocketAddress;

public class EventStoreReader {


    private String hostname = "127.0.0.1";
    private int port = 2113;
    private String login = "admin";
    private String password = "changeit";
    private String streamId = "hotelCreated";

    public EventStoreReader(String hostname, int port, String login, String password, String streamId) {
        this.hostname = hostname;
        this.port = port;
        this.login = login;
        this.password = password;
        this.streamId = streamId;
    }

    public String readEvent(final String streamName, final Integer eventNumber) {

        final ActorSystem system = ActorSystem.create();
        final Settings settings = new SettingsBuilder()
                .address(new InetSocketAddress(hostname, port))
                .defaultCredentials(login, password)
                .build();
        final ActorRef connection = system.actorOf(ConnectionActor.getProps(settings));
//        final ActorRef readResult = system.actorOf(Props.create(TcpConnection.ReadResult.class));
        final ActorRef readResult = system.actorOf(Props.create(ReadResult.class));
        final ReadEvent readEvent = new ReadEventBuilder(streamName)
                .number(eventNumber)
                .resolveLinkTos(false)
                .requireMaster(true)
                .build();

        final ReadEvent readEvent1 = new ReadEventBuilder(streamId)
                .first()
                .resolveLinkTos(false)
                .requireMaster(true)
                .build();

        try {
            connection.tell(readEvent, readResult);
            return readResult.toString();
        }catch (Exception ex){
            return null;
        }

    }
}
