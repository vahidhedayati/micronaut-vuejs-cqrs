Micronaut backend vuejs frontend application - CQRS
---

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


##### That is the entire working concept / theory explained above - event driven CQRS.


A basic theory can be seen here 

#### [Youtube will upload](TODO)


This project is incomplete will be marked as working - when this message is removed - and instructions are updated properly


Running app
----
##### Please refer to [pre-requirements](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/configure.md).

##### To start all applications in 2 sessions run:
Session  1
```
./gradlew hotel-read:run  userbase-read:run frontend:start gateway-command:run gateway-query:run hotel-write:run  userbase-write:run  --parallel
```

###### Advanced: 
##### When running on linux a process for node hangs on which also keeps jvms active - killing node kills all other jvms hanging off
##### this is all in 1 line to kill if found and start apps

Session  1
```
kill -9 $(netstat -pln 2>/dev/null |grep LISTEN|grep node|awk '{print $7}'|awk -F"/" '{print $1}');
./gradlew hotel-read:run  userbase-read:run frontend:start gateway-command:run gateway-query:run hotel-write:run  userbase-write:run   --parallel
```




The above will launch 1 instance of frontend vuejs site running on `localhost:3000` 
and a backend micronaut site running on port `localhost:{random}` a gateway micronaut app running on port 
`localhost:8080` 


##### frontend changed to resemble a grails vuejs site: will start up on port 3000
```
c:\xxx\micronaut\hotel-info>gradlew frontend:start

To manually start app using npm or yarn run:

micro-projects/hotel-info/frontend$ npm run dev   

micro-projects/hotel-info/frontend$ yarn run dev


```


##### To run Backend: will currently launch and bind to port {random}  - mutiple instances can be started

```
c:\xxxx\micronaut\hotel-info>gradlew backend:run --stacktrace

```


##### To run gateway app : will currently launch and bind to port  8080 - testing single instance only currently

```
c:\xxxx\micronaut\hotel-info>gradlew gateway:run --stacktrace

```


##### Attempt to build the existing microservice micronaut CRUD app to use CQRS:
This application at the moment has a working read / write for hotel object - 2 separated microservices applications - anything to save/update attempts to :
> Call command feature of hotel-write - generate hotel record saved on local h2 db - send hotel via kafka as an event to hotel-read application. hotel read gets event and stores/updates the record locally.

Save is currently working - update to be done - etc  



##### Please refer to [what CQRS is here](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/cqrs-explained.md).