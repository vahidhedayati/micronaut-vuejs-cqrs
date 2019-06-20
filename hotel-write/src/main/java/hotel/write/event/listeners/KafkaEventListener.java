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
    final EmbeddedServer embeddedServer;

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

    public KafkaEventListener(ObjectMapper objectMapper,EmbeddedServer embeddedServer) {
        this.objectMapper=objectMapper;
        this.embeddedServer=embeddedServer;
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


                    final Set<ConstraintViolation<Command>> constraintViolations = validator.validate(cmd);
                    if (constraintViolations.size() > 0) {
                        HashMap<String,String> violationMessages = new HashMap<>();

                        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                            violationMessages.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessage());
                            //violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
                        }
                        System.out.println(" HOTEL-WRITE VALIDATION ERROR - COMMAND BUS FAILED VALIDATION::: 01 ---->"+violationMessages);
                        // throw new ValidationException("Hotel is not valid:\n" + violationMessages);
                        //TODO - We need to websocket back and pickup
                        /// return HttpResponse.badRequest(violationMessages);
                        WebsocketMessage msg  =new WebsocketMessage();
                        msg.setCurrentUser(cmd.getCurrentUser());
                        msg.setErrors(violationMessages);
                        msg.setEventType("errorForm");
                        if (cmd.getSession()!=null ) {


                            System.out.println("Websocket Session found hooray - sending "+serializeMessage(msg));
                            cmd.getSession().send(serializeMessage(msg));
                        } else {
                           // WebSocketClient wsClient = embeddedServer.getApplicationContext().createBean(WebSocketClient, "https://ws-api.upstox.com")
                            /*
                            WebSocketClient client;
                            final String url = "ws://"+cmd.getHost()+":"+cmd.getPort()+"/ws/process";

                                client = new WebSocketClient();
                            }
                            System.out.print("Socket connection from: "+hostName+"\n");
                            //final String url = "ws://localhost:9000";
                            try {
                                client.open();
                                //Keep connection open and add it to the existing connCurrent Maps
                                BootService.addSocket(hostName,client);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return session.send("{ hostName:"+hostName+"}", MediaType.APPLICATION_JSON_TYPE);
                            */
                            //GatewayClient client = webSocketClient.connect(GatewayClient.class, "/ws/process".toString()).blockingFirst();
                            ChatClientWebSocket chatClient = webSocketClient.connect(ChatClientWebSocket.class, "/ws/process").blockingFirst();
                            chatClient.send(serializeMessage(msg));

                            System.out.println("Websocket Session found oh dear - this is an issue "+cmd.getCurrentUser());
                        }

                    } else {
                        if (cmd instanceof HotelSaveCommand) {
                            dao.save((HotelSaveCommand) cmd);
                        } else if (cmd instanceof HotelCreateCommand) {
                            dao.save((HotelCreateCommand) cmd);
                        } else if (cmd instanceof HotelUpdateCommand) {
                            dao.update((HotelUpdateCommand) cmd);
                        } else if (cmd instanceof HotelDeleteCommand) {
                            dao.delete((HotelDeleteCommand) cmd);
                        }
                    }
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
