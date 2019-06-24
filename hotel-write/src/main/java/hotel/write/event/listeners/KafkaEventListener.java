package hotel.write.event.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.write.commands.*;
import hotel.write.services.write.HotelService;
import hotel.write.websocket.ChatClientWebSocket;
import hotel.write.websocket.WebsocketMessage;
import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.runtime.server.EmbeddedServer;
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
import java.util.*;

@ThreadSafe
@KafkaListener
public class KafkaEventListener implements ConsumerRebalanceListener, ConsumerAware {

    private final ObjectMapper objectMapper;

    @Inject
    @Client("http://localhost:8082")
    RxWebSocketClient webSocketClient;

    Map<String, Class> commandClasses = new HashMap<String,Class>() {
        {
            put(HotelCreateCommand.class.getSimpleName(), HotelCreateCommand.class);
            put(HotelSaveCommand.class.getSimpleName(), HotelSaveCommand.class);
            put(HotelUpdateCommand.class.getSimpleName(), HotelUpdateCommand.class);
            put(HotelDeleteCommand.class.getSimpleName(), HotelDeleteCommand.class);
        }
    };

    public KafkaEventListener(ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
    }
    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
    }

    @Inject
    private HotelService dao;

    @Topic("hotel")
    public void consume(@KafkaKey String hotelCode,  String hotelCreatedEvent) {
        if (hotelCode!=null &&  hotelCode.contains("_")) {
            String eventType = hotelCode.split("_")[0];
            if (eventType!=null) {
                JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                        .orElseThrow(() -> new IllegalStateException("No JSON codec found"));
                Command cmd = (Command) mediaTypeCodec.decode(commandClasses.get(eventType),hotelCreatedEvent);
                if (hotelCreatedEvent !=null ) {
                    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

                    WebsocketMessage msg  =new WebsocketMessage();
                    msg.setCurrentUser(cmd.getCurrentUser());

                    final Set<ConstraintViolation<Command>> constraintViolations = validator.validate(cmd);
                    if (constraintViolations.size() > 0) {
                        HashMap<String,String> violationMessages = new HashMap<>();

                        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                            violationMessages.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessage());
                            //violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
                        }

                        msg.setErrors(violationMessages);
                        msg.setEventType("errorForm");
                    } else {
                        /**
                         * a note of warning about this id based on creation this is currently returning ID of record on
                         * hotel-write which is the write side or should I say wrong :) side of the equation
                         * it should be looking for an id on hotel-read for the id
                         */
                        msg.setEventType("successForm");
                        if (cmd instanceof HotelSaveCommand) {
                            dao.save((HotelSaveCommand) cmd);
                            msg.setId(dao.findByCode(((HotelSaveCommand) cmd).getCode()).map(h->h.getId()));
                        } else if (cmd instanceof HotelCreateCommand) {
                            dao.save((HotelCreateCommand) cmd);
                            msg.setId(dao.findByCode(((HotelCreateCommand) cmd).getCode()).map(h->h.getId()));
                        } else if (cmd instanceof HotelUpdateCommand) {
                            dao.update((HotelUpdateCommand) cmd);
                            msg.setId(Optional.of(((HotelUpdateCommand) cmd).getId()));
                        } else if (cmd instanceof HotelDeleteCommand) {
                            dao.delete((HotelDeleteCommand) cmd);
                            msg.setId(Optional.of(((HotelUpdateCommand) cmd).getId()));
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
