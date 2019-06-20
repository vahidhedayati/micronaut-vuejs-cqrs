package hotel.write.websocket;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.*;
import io.reactivex.Single;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@ClientWebSocket("/ws/{topic}")
public abstract class ChatClientWebSocket implements AutoCloseable {

    private WebSocketSession session;
    private HttpRequest request;
    private String topic;
    private Collection<String> replies = new ConcurrentLinkedQueue<>();

    @OnOpen
    public void onOpen(String topic,  WebSocketSession session, HttpRequest request) {
        this.topic = topic;

        this.session = session;
        this.request = request;
    }
    public abstract void send(String message);
    public String getTopic() {
        return topic;
    }


    public Collection<String> getReplies() {
        return replies;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public HttpRequest getRequest() {
        return request;
    }

    @OnMessage
    public void onMessage(
            String message) {
        replies.add(message);
    }

}