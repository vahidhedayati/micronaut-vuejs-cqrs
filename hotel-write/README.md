```
11:07:13.806 [nioEventLoopGroup-1-3] INFO  i.m.d.registration.AutoRegistration - Registered service [commandapp] with Consul
Create Hotel handler
HotelWriteEndpoint save
bus.handleCommand new CreateHotelCommand
getCommandName: CreateHotelCommand
getCommandName: CreateHotelCommand
handle command: CreateHotelCommand
getCommandName: CreateHotelCommand
hotel:null,AAAAAAAAAAAAA
handleCommand AbstractCommandHandler - save is called hotel.write.event.commands.HotelSaveCommand@360d46bb 
--- from abstract class save hotel:null,AAAAAAAAAAAAA
```

[handleCommand AbstractCommandHandler - save is called hotel.write.event.commands.HotelSaveCommand@360d46bb --- from abstract class save hotel:null,AAAAAAAAAAAAA](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/commandservice/src/main/java/commandservice/commands/AbstractCommandHandler.java#L32-L40)


```
Adding hotel to arrayList
handleCommand AbstractCommandHandler - buildEvent hotel.write.event.commands.HotelSaveCommand@360d46bb
build event hotel:null,AAAAAAAAAAAAA
publisher.publish(hotel.write.event.HotelCreatedEvent@236eeaae
 KafkaPublisher publishing: hotel.write.event.HotelCreatedEvent@236eeaae
getEventCode: AAA
11:07:39.244 [pool-1-thread-5] INFO  o.a.k.c.producer.ProducerConfig - ProducerConfig values: 
        acks = 1
        batch.size = 16384
        bootstrap.servers = [localhost:9092]
        buffer.memory = 33554432
        client.dns.lookup = default
        client.id = 
        compression.type = none
        connections.max.idle.ms = 540000
        delivery.timeout.ms = 120000
        enable.idempotence = false
        interceptor.classes = []
        key.serializer = class org.apache.kafka.common.serialization.StringSerializer
        linger.ms = 0
        max.block.ms = 60000
        max.in.flight.requests.per.connection = 5
        max.request.size = 1048576
        metadata.max.age.ms = 300000
        metric.reporters = []
        metrics.num.samples = 2
        metrics.recording.level = INFO
        metrics.sample.window.ms = 30000
        partitioner.class = class org.apache.kafka.clients.producer.internals.DefaultPartitioner
        receive.buffer.bytes = 32768
        reconnect.backoff.max.ms = 1000
        reconnect.backoff.ms = 50
        request.timeout.ms = 30000
        retries = 2147483647
        retry.backoff.ms = 100
        sasl.client.callback.handler.class = null
        sasl.jaas.config = null
        sasl.kerberos.kinit.cmd = /usr/bin/kinit
        sasl.kerberos.min.time.before.relogin = 60000
        sasl.kerberos.service.name = null
        sasl.kerberos.ticket.renew.jitter = 0.05
        sasl.kerberos.ticket.renew.window.factor = 0.8
        sasl.login.callback.handler.class = null
        sasl.login.class = null
        sasl.login.refresh.buffer.seconds = 300
        sasl.login.refresh.min.period.seconds = 60
        sasl.login.refresh.window.factor = 0.8
        sasl.login.refresh.window.jitter = 0.05
        sasl.mechanism = GSSAPI
        security.protocol = PLAINTEXT
        send.buffer.bytes = 131072
        ssl.cipher.suites = null
        ssl.enabled.protocols = [TLSv1.2, TLSv1.1, TLSv1]
        ssl.endpoint.identification.algorithm = https
        ssl.key.password = null
        ssl.keymanager.algorithm = SunX509
        ssl.keystore.location = null
        ssl.keystore.password = null
        ssl.keystore.type = JKS
        ssl.protocol = TLS
        ssl.provider = null
        ssl.secure.random.implementation = null
        ssl.trustmanager.algorithm = PKIX
        ssl.truststore.location = null
        ssl.truststore.password = null
        ssl.truststore.type = JKS
        transaction.timeout.ms = 60000
        transactional.id = null
        value.serializer = class io.micronaut.configuration.kafka.serde.JsonSerde

11:07:39.280 [pool-1-thread-5] INFO  o.a.kafka.common.utils.AppInfoParser - Kafka version : 2.1.1
11:07:39.280 [pool-1-thread-5] INFO  o.a.kafka.common.utils.AppInfoParser - Kafka commitId : 21234bee31165527
11:07:39.663 [kafka-producer-network-thread | producer-1] WARN  o.apache.kafka.clients.NetworkClient - [Producer clientId=producer-1] Error while fetching metadata with correlation id 2 : {hotel=LEADER_NOT_AVAILABLE}mmandservice:run
11:07:39.663 [kafka-producer-network-thread | producer-1] INFO  org.apache.kafka.clients.Metadata - Cluster ID: ZC1VX1ngTteGltWvvKKNMQ
getEventId: null
11:07:39.782 [pool-1-thread-5] ERROR i.m.r.intercept.RecoveryInterceptor - Type [hotel.write.event.client.EventClient$Intercepted] executed with error: Exception sending producer record for method [void sendCode(String hotelCode,AbstractEvent hotelEvent)]: Error serializing object to JSON: (was java.lang.NullPointerException) (through reference chain: hotel.write.event.HotelCreatedEvent["eventId"])
io.micronaut.messaging.exceptions.MessagingClientException: Exception sending producer record for method [void sendCode(String hotelCode,AbstractEvent hotelEvent)]: Error serializing object to JSON: (was java.lang.NullPointerException) (through reference chain: hotel.write.event.HotelCreatedEvent["eventId"])
        at io.micronaut.configuration.kafka.intercept.KafkaClientIntroductionAdvice.wrapException(KafkaClientIntroductionAdvice.java:518)
        at io.micronaut.configuration.kafka.intercept.KafkaClientIntroductionAdvice.intercept(KafkaClientIntroductionAdvice.java:403)
        at io.micronaut.aop.MethodInterceptor.intercept(MethodInterceptor.java:40)
        at io.micronaut.aop.chain.InterceptorChain.proceed(InterceptorChain.java:146)
        at io.micronaut.retry.intercept.RecoveryInterceptor.intercept(RecoveryInterceptor.java:73)
        at io.micronaut.aop.MethodInterceptor.intercept(MethodInterceptor.java:40)
        at io.micronaut.aop.chain.InterceptorChain.proceed(InterceptorChain.java:146)
        at hotel.write.event.client.EventClient$Intercepted.sendCode(Unknown Source)
        at hotel.write.event.client.KafkaPublisher.publish(KafkaPublisher.java:19)
        at hotel.write.commandHandlers.AbstractCommandHandler.publish(AbstractCommandHandler.java:51)
        at hotel.write.commandHandlers.AbstractCommandHandler.handleCommand(AbstractCommandHandler.java:38)
        at hotel.write.cqrs.bus.BusImpl.handleCommand(BusImpl.java:32)
        at hotel.write.services.write.HotelService.addHotel(HotelService.java:17)
        at hotel.write.controller.HotelWriteEndpoint.save(HotelWriteEndpoint.java:25)
        at hotel.write.controller.$HotelWriteEndpointDefinition$$exec1.invokeInternal(Unknown Source)
        at io.micronaut.context.AbstractExecutableMethod.invoke(AbstractExecutableMethod.java:144)
        at io.micronaut.context.DefaultBeanContext$BeanExecutionHandle.invoke(DefaultBeanContext.java:2704)
        at io.micronaut.web.router.AbstractRouteMatch.execute(AbstractRouteMatch.java:295)
        at io.micronaut.web.router.RouteMatch.execute(RouteMatch.java:122)
```
