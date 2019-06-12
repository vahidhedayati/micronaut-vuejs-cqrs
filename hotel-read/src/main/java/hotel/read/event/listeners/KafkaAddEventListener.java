package hotel.read.event.listeners;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
//
// @KafkaListener
public class KafkaAddEventListener {
        //implements ConsumerRebalanceListener, ConsumerAware {

    //private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    //private int listenerCount = 3;
   //// AtomicBoolean ranChanges = new AtomicBoolean(false);
   // AtomicInteger currentCount = new AtomicInteger(0);
   // ExecutorService executorService = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3),
    //        new ThreadPoolExecutor.DiscardOldestPolicy());

   // public KafkaAddEventListener(ExecutorService executorService) {
    //    this.executorService = executorService;
    //}
   //Collection<TopicPartition> allPartitions=new ArrayList();


    /*
    @GuardedBy("kafkaConsumers")
    private final Set<Consumer> kafkaConsumers = new HashSet<>();

    private Consumer consumer;

   @Override
   public void setKafkaConsumer(@Nonnull final Consumer consumer) {
       this.consumer=consumer;
       synchronized (kafkaConsumers) {
           this.kafkaConsumers.add(consumer);
       }
   }

    //protected static final Logger LOG = LoggerFactory.getLogger(KafkaAddEventListener.class);

    @Inject
    private Hotels dao;
    //private QueryHotelViewDao dao;

    @Topic("hotelCreated1")
    public void consume( @KafkaKey String hotelCode, @Body HotelCreatedEvent hotelCreatedEvent) {
        if (hotelCode!=null) {
            // LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
            //System.out.println("READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  hotelCreated ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
            System.out.println("READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  hotelCreated --- "+hotelCode);
            HotelCreatedCommand cmd =  hotelCreatedEvent.getDtoFromEvent();
            if (cmd !=null ) {
                Hotel h= cmd.createHotel();
                if (h !=null ) {
                    dao.save(h);
                }
            }



        }

    }

*/
/*
    @Topic("hotelEdit1")
    public void consumeEdit( @KafkaKey String hotelCode, @Body HotelUpdateCommandEvent hotelCreatedEvent) {
        if (hotelCode!=null) {
            //  LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER consumeEdit "+hotelCode);
            //System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER consumeEdit ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
            System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER consumeEdit ---"+hotelCode);
            HotelUpdateCommand cmd =  hotelCreatedEvent.getDtoFromEvent();
            if (cmd !=null ) {
                dao.update(cmd);
            }

        }
    }

    @Topic("hotelDelete1")
    public void consumeDelete( @KafkaKey String hotelCode, @Body HotelDeletedCommandEvent hotelCreatedEvent) {
        if (hotelCode!=null) {
            //   LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelDelete " + hotelCode);
            //System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelDelete ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
            System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelDelete ---"+hotelCode);
            HotelDeleteCommand cmd =  hotelCreatedEvent.getDtoFromEvent();
            if (cmd !=null ) {
                dao.delete(cmd);
            }
        }
    }

*/
/*
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("onPartitionsRevoked------------------------------------------------------------------------------------------------");
        // partitions.iterator().forEachRemaining();
        // save offsets here


        for(TopicPartition partition: partitions) {
            synchronized (partition) {
              // kafkaConsumers.forEach(consumer -> {
                    //synchronized (kafkaConsumers) {
                        System.out.println("  parition position : " + partition + ' ');
                        // + consumer.position(partition));
                   // }
               // });
            }
            //   saveOffsetInExternalStore(consumer.position(partition));
        }
    }
    */


    /**
     * This triggers a new node to build h2 db up based on existing received kafka events
     * @param partitions
     *//*
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

       // Collection<String> collection= new ArrayList();
        //Collection<TopicPartition> topicPartition= new ArrayList();
        //int i=0;
        for (TopicPartition partition : partitions) {

            System.out.println(" Topic " + partition.topic() + " polling");
            synchronized (consumer) {
                //this.consumer.
                this.consumer.subscribe(Arrays.asList(partition.topic()));
            }
            //boolean flag = true;
            //while (true) {

            ConsumerRecords<String, String> records = this.consumer.poll(100);
            //if (flag) {
            try {
                System.out.println(" Topic " + partition.topic() + " seekBegin");
                this.consumer.seek(partition,1);
                //this.consumer.wakeup();

                //  flag = false;
            } catch (Exception e) {
                rewind(records);
                //Thread.sleep(100);
            }
            //}
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

            System.out.println("Topics done - subscribing: ");

            //TopicPartition tp = new TopicPartition(partition.topic(), partition.partition());
            //topicPartition.add(partition);
            //List<TopicPartition> tps = Arrays.asList(tp);
            //Consumer current=this.consumer;
            //collection.add(partition.topic());
            //}

            //
            //if (!allPartitions.contains(partition) ) {
               // allPartitions.add(partition);
            //}
               // this.consumer.seek(partition, 1);

                //kafkaConsumers.forEach(consumer -> {
                //synchronized (consumer) {
                //consumer.seek(partition, 1);
                // }
                //});
           // }
           // System.out.println("counter check: "+i+" "+listenerCount);
            //if (currentCount.incrementAndGet()==listenerCount) {

           // }


        }



    }
    private void rewind(ConsumerRecords<String, String> records) {
        records.partitions().forEach(partition -> {
            long offset = records.records(partition).get(0).offset();
            consumer.seek(partition, offset);
        });
    }
    */

}
