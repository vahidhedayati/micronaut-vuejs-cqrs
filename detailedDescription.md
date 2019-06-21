##### Entire working concept / theory explained  - event driven CQRS micronaut vuejs kafka consul.
This project contains of 6 micronaut microservice applications and 1 vuejs front end application.
All contains or executable via gradle.

It is an event driven CQRS CRUD microservice application. The standard stuff you see in micronaut applications communicating have been replaced with kafka events.

To outline basics and what happens.


> User -> talks with vuejs app called `frontend` on port `3000` i.e `http://localhost:3000/`
The user mimiks rest calls via either post for write or get to read. Post has eventType appended which deciphers what type of command to use to trigger kafka event. 

> `frontend` talks to 2 end points:

>  -- For reading any `READ` request it talks to defined host port `http://localhost:8081` - this is `gateway-query` microservice.

>  -- For writing any `WRITE` request it talks to defined host port `http://localhost:8082` - this is `gateway-command` microservice.


> The user also when connected to port `3000` triggers a websocket connection the same `controller` they hit for `gateway-command` on  `ws://localhost:8082`


> `gateway-read` does use `httpClient` to talk in standard way to talk to controllers on any app i.e.:

> `hotel-read`,  `user-read` and so on. Each app has a fully built up model of objects it would be listing / showing - in the case of hotel-read it maps in user details from userfile - 

> `gateway-write` looks at what has been posted according to `evenType parameter` generates required `command object` that is then sent to given `kafka topic`
The topics are picked up by relevant `hotel-write` or `user-write` app depending on `topic` and remaps String json back to real `command object`.
write microservice would then use the `command object` to validate actual object. 

------------
> Object validation on write side or command handler and socket clients from write microservices back to `gateway-command` on  `ws://localhost:8082`:

> if the object is valid - if it was a new record RX java `optional` call made to pick up the new id of the newly added record update etc picks up provided id.
It connects to gateway-command and transmits a succesful message to websocket server. 

> If the object is invalid - the validation errors are gather and a failure socket message is transmitted to `gateway-command`

------------
> Websocket communication to user from `gateway-command` 

> The user when hitting hotel generated a unique currentUser value this got sent as part of socket connection, when user posted hotel to add new one
the currentUser was appended and became part of the `Command object`. When it fails it generates a socket back to gateway - 
gateway now finds in its connected sockets the currentUser matching error message sent back the user in `Command.java` vs user in `WebsocketMessage.java`
it transmits a message to that socket client i.e. user on vuejs site - vuejs - picks up event re-enables submit button displays success or 
failure messages and locally appends record to its records if successful. 


