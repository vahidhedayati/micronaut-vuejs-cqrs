package hotel.write.event.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.write.event.commands.CommandRoot;
import hotel.write.services.write.HotelService;
import hotel.write.websocket.ChatClientWebSocket;
import hotel.write.websocket.WebsocketMessage;
import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.websocket.RxWebSocketClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

@ThreadSafe
@KafkaListener
public class KafkaEventListener implements ConsumerRebalanceListener, ConsumerAware {

    private final ObjectMapper objectMapper;

    @Inject
    @Client("http://localhost:8082")
    RxWebSocketClient webSocketClient;


    public KafkaEventListener(ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
    }

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
    }

    @Inject
    private HotelService bus;



    @Topic("hotel")
    public  <T extends CommandRoot> void  consume(@KafkaKey String hotelCode,T cmd) {
        if (cmd !=null ) {
            final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

            WebsocketMessage msg  =new WebsocketMessage();
            msg.setCurrentUser(cmd.getCurrentUser());

            final Set<ConstraintViolation<T>> constraintViolations = validator.validate(cmd);
            if (constraintViolations.size() > 0) {
                HashMap<String,String> violationMessages = new HashMap<>();

                for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                    violationMessages.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessage());
                }
                msg.setErrors(violationMessages);
                msg.setEventType("errorForm");
            } else {
                msg.setEventType("successForm");
                bus.handleCommand(cmd);
            }
            ChatClientWebSocket chatClient = webSocketClient.connect(ChatClientWebSocket.class, "/ws/process").blockingFirst();
            chatClient.send(serializeMessage(msg));
        }
    }


    public String serializeMessage(WebsocketMessage command) {
        String json;
        try {
            json = objectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        //for(TopicPartition partition: partitions) {
        //}
    }


    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition partition : partitions) {
            synchronized (consumer) {
                this.consumer.subscribe(Arrays.asList(partition.topic()));
            }
            ConsumerRecords<String, String> records = this.consumer.poll(100);
            try {
                this.consumer.seek(partition,1);
            } catch (Exception e) {
                rewind(records);
            }
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
    private void rewind(ConsumerRecords<String, String> records) {
        records.partitions().forEach(partition -> {
            long offset = records.records(partition).get(0).offset();
            consumer.seek(partition, offset);
        });
    }

}
