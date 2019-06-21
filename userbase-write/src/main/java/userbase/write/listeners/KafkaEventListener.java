package userbase.write.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.websocket.RxWebSocketClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import userbase.write.commands.Command;
import userbase.write.commands.UserDeleteCommand;
import userbase.write.commands.UserSaveCommand;
import userbase.write.commands.UserUpdateCommand;
import userbase.write.service.UserService;
import userbase.write.websocket.ChatClientWebSocket;
import userbase.write.websocket.WebsocketMessage;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@ThreadSafe
@KafkaListener
public class KafkaEventListener implements ConsumerRebalanceListener, ConsumerAware {

    private final ObjectMapper objectMapper;

    /**
     * TODO this is currently hard wired to something that is dynamic in command object host/port
     */
    @Inject
    @Client("http://localhost:8082")
    RxWebSocketClient webSocketClient;

    public KafkaEventListener(ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
    }

    Map<String, Class> commandClasses = new HashMap<String,Class>() {
        {

            put(UserSaveCommand.class.getSimpleName(), UserSaveCommand.class);
            put(UserUpdateCommand.class.getSimpleName(), UserUpdateCommand.class);
        }
    };

    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
    }

    @Inject
    private UserService dao;

    @Topic("user")
    public void consume(@KafkaKey String hotelCode,  String hotelCreatedEvent) {
        if (hotelCode!=null &&  hotelCode.contains("_")) {
            String eventType = hotelCode.split("_")[0];
            if (eventType!=null) {
                // LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
                System.out.println("_____> USERWRITE  EVENT: "+eventType+"--------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  --- "+hotelCode+ " -- event "+hotelCreatedEvent);
                JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                        .orElseThrow(() -> new IllegalStateException("No JSON codec found"));

                Command cmd = (Command) mediaTypeCodec.decode(commandClasses.get(eventType),hotelCreatedEvent);
                //System.out.println("Default save of hotel in hotel-write ---------------- command "+cmd);

                if (hotelCreatedEvent !=null ) {
                    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

                    WebsocketMessage msg  =new WebsocketMessage();
                    msg.setCurrentUser(cmd.getCurrentUser());

                    final Set<ConstraintViolation<Command>> constraintViolations = validator.validate(cmd);
                    if (constraintViolations.size() > 0) {
                       // Set<String> violationMessages = new HashSet<String>();
                        HashMap<String,String> violationMessages = new HashMap<>();

                        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                            violationMessages.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessage());
                           // violationMessages.add(constraintViolation.getMessage());
                            //violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
                        }
                        System.out.println(" USER-WRITE VALIDATION ERROR - COMMAND BUS FAILED VALIDATION::: 01 ---->"+violationMessages);

                        msg.setErrors(violationMessages);
                        msg.setEventType("errorForm");
                    } else {
                        msg.setEventType("successForm");
                        if (cmd instanceof UserSaveCommand) {
                            dao.save((UserSaveCommand) cmd);
                            msg.setId(dao.findByUsername(((UserSaveCommand) cmd).getUsername()).map(h->h.getId()));
                        } else if (cmd instanceof UserUpdateCommand) {
                            dao.update((UserUpdateCommand) cmd);
                            msg.setId(Optional.of(((UserUpdateCommand) cmd).getId()));
                        } else if (cmd instanceof UserDeleteCommand) {
                            dao.delete((UserDeleteCommand) cmd);
                            msg.setId(Optional.of(((UserDeleteCommand) cmd).getId()));
                        }
                    }
                    ChatClientWebSocket chatClient = webSocketClient.connect(ChatClientWebSocket.class, "/ws/process").blockingFirst();
                    chatClient.send(serializeMessage(msg));
                }
            }

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
        System.out.println("onPartitionsRevoked------------------------------------------------------------------------------------------------");
        // partitions.iterator().forEachRemaining();
        // save offsets here
        for(TopicPartition partition: partitions) {
            synchronized (partition) {
                System.out.println("  onPartitionsRevoked parition : " + partition + ' ');
                // + consumer.position(partition));
                //consumer.seek(partition,1);
            }
        }
    }


    /**
     * This triggers a new node to build h2 db up based on existing received kafka events
     * @param partitions
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition partition : partitions) {
            System.out.println("onPartitionsAssigned  Topic " + partition.topic() + " polling");
            synchronized (consumer) {
                //this.consumer.
                this.consumer.subscribe(Arrays.asList(partition.topic()));
            }
            ConsumerRecords<String, String> records = this.consumer.poll(100);
            try {
                System.out.println(" Topic " + partition.topic() + " seekBegin");
                this.consumer.seek(partition,1);
            } catch (Exception e) {
                rewind(records);
                //Thread.sleep(100);
            }
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

            System.out.println("Topics done - subscribing: ");
        }
    }
    private void rewind(ConsumerRecords<String, String> records) {
        records.partitions().forEach(partition -> {
            long offset = records.records(partition).get(0).offset();
            consumer.seek(partition, offset);
        });
    }

}
