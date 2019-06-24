##### Entire working concept / theory explained  - event driven CQRS micronaut vuejs kafka consul.
This project contains of 6 micronaut microservice applications and 1 vuejs front end application.
All contains or executable via gradle.

It is an event driven CQRS CRUD microservice application. The standard stuff you see in micronaut applications communicating have been replaced with kafka events 
at the very least for the write aspect of this microservice application.

To outline basics and what happens.


> User -> talks with vuejs app called `frontend` on port `3000` i.e `http://localhost:3000/`
The user mimiks rest calls via either `POST` for write or `GET` to read.
 
> Post has eventType appended which deciphers what type of command to use to when reading posted object via kafka event. 

> `frontend` talks to 2 end points:

>  -- For reading any `READ` request it talks to defined host port `http://localhost:8081` - this is `gateway-query` microservice.

>  -- For writing any `WRITE` request it talks to defined host port `http://localhost:8082` - this is `gateway-command` microservice.


> The user also when connected to port `3000` triggers a websocket connection the same `controller`. The user also hits `gateway-command` on  `ws://localhost:8082` as they load up `Hotel.vue`

-----------------

> `gateway-read` uses `httpClient` to talk in standard way to talk to controllers on any app i.e.:

> `hotel-read`,  `user-read` and so on. Each app has a fully built up model of objects it would be listing / showing - in the case of hotel-read it maps in user details from userfile - 

-----------------

> `gateway-write` looks at what has been posted according to `evenType parameter` generates required `command object` that is then sent to given `kafka topic`
The topics are picked up by relevant `hotel-write` or `user-write` microservice applications, depending on `topic` and remaps String json back to real `command object`.
write microservice would then use the `command object` to validate actual object. 

------------
> Object validation on write side of command object happens in command handler `KafkaEventListener`. The listener also has a websocket client to communicate to `gateway-command` on  `ws://localhost:8082`:

> if the object is valid - if it was a new record RX java `optional` call made to pick up the new id of the newly added record update etc which picks up newly created id -
this does appear to be wrong logic since we would want id on read side to appear on user's screen for their new record.

> If the object is invalid - the validation errors are gather and a failure

> It connects to gateway-command and transmits a succesful / failure message to websocket server. 
------------

> Websocket communication to user from `gateway-command` 

> When user opened `Hotel.vue` they made a websocket connection as a client just like `hotel-write`  `KafkaEventListener` does above when transmitting it's messages.
As they hit the page they get assigned a unique random id which is sent as part of their initial socket connection. The socket listener aka `GatewayController` 
on `gateway-command` picks up this randomId and internally assigns ID and their current socket to a concurrentMap. 
when any app in this case `hotel-write` `KafkaEventListener`  publishes a success failure socket event and sends back to this socket controller.
 The response is picked up and the random Id assigned as part of their initial connection has always also been posted as part of any save/update/delete function.
 The random user is picked out from the newly received websocket message that was sent by the write application and from that id the websocket session of the underlying user is found.
 The end user is then sent a websocket message with success/failure response.
 Hotel.vue on user screen picks out the socket event and re-enables form submit button and displays any success failure messages.
