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


> The user also when connected to port `3000` triggers a websocket connection the same `controller`.

-----------------

> `gateway-read` uses `httpClient` to talk in standard way to talk to controllers on any app i.e.:

> `hotel-read`,  `user-read` and so on. Each app has a fully built up model of objects it would be listing / showing - in the case of hotel-read it maps in user details from userfile - 

-----------------

> `gateway-write` receives string json and converts to command object - uses HTTPEventPublisher which binds to micronaut http clients locally and transmits command object to any given http client which acts as listener on receiving end.  The sessions retransmits response from http client of remote host back to user which returns either success or failure with errors. 


> Each aggregate route listen in on HttpListener and publish received specific command to ApplicationEventHander aka AbstractCommandHandler - this gets picked up by specific command handlers and 
1. adds to DB
2. republishes the command as event object back into kafka

> KafakEventListener running on all read nodes, when topic of interest comes in, it is picked up and published to ApplicationEventHandler aka AbstractEventHandler, a relevant handler i.e. HotelUpdatedHandler picks up and stores event locally.

