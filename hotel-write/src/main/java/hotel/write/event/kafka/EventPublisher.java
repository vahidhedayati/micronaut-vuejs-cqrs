package hotel.write.event.kafka;


import hotel.write.commands.Command;

public  interface EventPublisher {
    <T extends Command> void publish(String topic, T command);
}