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

    /**
     * TODO this is currently hard wired to something that is dynamic in command object host/port
     */
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


   // @GuardedBy("kafkaConsumers")
  //  private final Set<Consumer> kafkaConsumers = new HashSet<>();

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
       // synchronized (kafkaConsumers) {
       //     this.kafkaConsumers.add(consumer);
        //}
    }

    //protected static final Logger LOG = LoggerFactory.getLogger(KafkaEventListener.class);

    @Inject
    private HotelService dao;

    @Topic("hotel")
    public void consume(@KafkaKey String hotelCode,  String hotelCreatedEvent) {
        if (hotelCode!=null &&  hotelCode.contains("_")) {
            String eventType = hotelCode.split("_")[0];
            if (eventType!=null) {
                // LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
                System.out.println("_____> HOTELWRITE  EVENT: "+eventType+"--------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  --- "+hotelCode+ " -- event "+hotelCreatedEvent);
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
                        System.out.println(" HOTEL-WRITE VALIDATION ERROR - COMMAND BUS FAILED VALIDATION::: 01 ---->"+violationMessages);
                        // throw new ValidationException("Hotel is not valid:\n" + violationMessages);
                        /// return HttpResponse.badRequest(violationMessages);

                        //sending back erros via websocket as json well converted to json in Gateway controller on gateway-command
                        msg.setErrors(violationMessages);
                        msg.setEventType("errorForm");
                    } else {
                        /**
                         * a note of warning about this id based on creation this is currently returning ID of record on
                         * hotel-write which is the write side or should I say wrong :) side of the equation
                         * it should be looking for an id on hotel-read so write is wrong read is right hehehehehe
                         * anyhow it is conceptual so perhaps the validation should really be happening on read
                         * or or or maybe the front end when it has a save goes off and gets id for it rather than this way
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
