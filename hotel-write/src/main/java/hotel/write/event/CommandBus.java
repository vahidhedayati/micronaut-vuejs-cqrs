package hotel.write.event;

public interface CommandBus<T> {
    void  handleCommand(T  cmd);
}
