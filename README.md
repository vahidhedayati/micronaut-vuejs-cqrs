Micronaut backend vuejs frontend application - [CQRS](http://codebetter.com/gregyoung/2010/02/16/cqrs-task-based-uis-event-sourcing-agh/)
---


Running app
----

##### Run Consul

To run first either install consul locally and run `./consul agent dev`
#####  Consul on  Windows : you will need [consul.exe](https://www.consul.io/docs/install/index.html) then open cmd and run:

```

consul agent -dev -node  machine

```

or if you have installed docker simply run `sudo docker run -p 8500:8500 consul`

 ##### Run Kafka
 
 Please ensure you are also running kafka
 -> sudo /opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties
 
 -> sudo /opt/kafka/bin/kafka-topics.sh --list --zookeeper localhost:2181

 #### Kafka on windows: [Download kafka](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.2.1/kafka_2.11-2.2.1.tgz)

```
#open  cmd and run (cmd2)

c:\dev\kafka\bin\windows>zookeeper-server-start.bat  c:\dev\kafka\config\zookeeper.properties


# open cmd and run (cmd3)

c:\dev\kafka\bin\windows>kafka-server-start.bat c:\dev\kafka\config\server.properties


```


 Or using docker
 
 First of all we need to set up a local Kafka
 
 We create a private network
 ```
 docker network create app-tier --driver bridge
 ```
 We will use that network so Kafka can see our Zookeeper server just using the name we declared while starting it.
 
 Start our Zookeeper instance:

```
 docker run -d --name zookeeper-server \
     --network app-tier \
     -p 127.0.0.1:2181:2181/tcp \
     -p 127.0.0.1:2888:2888/tcp \
     -p 127.0.0.1:3888:3888/tcp \
     -e ALLOW_ANONYMOUS_LOGIN=yes \
     bitnami/zookeeper:latest
```

 Start our Kafka server:

``` 
 docker run -d --name kafka-server \
     --network app-tier \
     -p 127.0.0.1:9092:9092/tcp \
     -e KAFKA_ZOOKEEPER_CONNECT=zookeeper-server:2181 \
     -e ALLOW_PLAINTEXT_LISTENER=yes \
     bitnami/kafka:latest
```

 We can trigger a Kafka manager so we can see what is going on in our cluster
 
```
 docker run -d --name kafka-manager \
        --network app-tier \
        -p 9000:9000  \
 	   -e ZK_HOSTS="zookeper-server:2181" \
 	   -e APPLICATION_SECRET=letmein sheepkiller/kafka-manager
``` 
 As we are running now in a private network, it does not allow us to access using localhost nor the name of the container. We need to find out which is the hostname assigned by docker.
 
 That can be easily retrieved inspecting our container
 ```
 $ docker inspect kafka-server | grep Hostname | grep -v Path
 ```
 #Output
 ```
 "Hostname": "89cc3866239e",
 ```

##### To start all applications in 2 sessions run:
Session  1
```
./gradlew frontend:start backend:run gateway:run userbase:run  --parallel
```
Session  2
```
./gradlew  queryservice:run  commandservice:run  --parallel
```
# Advanced: 
# When running on linux a process for node hangs on which also keeps jvms active - killing node kills all other jvms hanging off
# this is all in 1 line to kill if found and start apps

Session  1
```
kill -9 $(netstat -pln 2>/dev/null |grep LISTEN|grep node|awk '{print $7}'|awk -F"/" '{print $1}'); ./gradlew frontend:start backend:run gateway:run userbase:run
```

Session  2 
 ```
 ./gradlew  queryservice:run  commandservice:run  --parallel
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



##### CQRS Command and Query Responsibility Segregation ([CQRS](http://codebetter.com/gregyoung/2010/02/16/cqrs-task-based-uis-event-sourcing-agh/) as described here)
> Starting with CQRS, CQRS is simply the creation of two objects where there was previously only one. 
The separation occurs based upon whether the methods are a command or a query (the same definition that 
is used by Meyer in Command and Query Separation, a command is any method that mutates state and a query 
is any method that returns a value).

> When most people talk about CQRS they are really speaking about applying the CQRS pattern to the object that
 represents the service boundary of the application. Consider the following pseudo-code service definition.
 
```
CustomerService

void MakeCustomerPreferred(CustomerId) 
Customer GetCustomer(CustomerId) 
CustomerSet GetCustomersWithName(Name) 
CustomerSet GetPreferredCustomers() 
void ChangeCustomerLocale(CustomerId, NewLocale) 
void CreateCustomer(Customer) 
void EditCustomerDetails(CustomerDetails)
```
 

##### Applying CQRS on this would result in two services

```
CustomerWriteService

void MakeCustomerPreferred(CustomerId) 
void ChangeCustomerLocale(CustomerId, NewLocale) 
void CreateCustomer(Customer) 
void EditCustomerDetails(CustomerDetails)
```

```
CustomerReadService

Customer GetCustomer(CustomerId) 
CustomerSet GetCustomersWithName(Name) 
CustomerSet GetPreferredCustomers()
```

> That is it. That is the entirety of the CQRS pattern. There is nothing more to it than that… 
Doesn’t seem nearly as interesting when we explain it this way does it? This separation however 
enables us to do many interesting things architecturally, the largest is that it forces a break of the mental
 retardation that because the two use the same data they should also use the same data model.

> The largest possible benefit though is that it recognizes that their are different architectural properties 
when dealing with commands and queries … for example it allows us to host the two services differently eg: 
we can host the read service on 25 servers and the write service on two. The processing of commands and queries
 is fundamentally asymmetrical, and scaling the services symmetrically does not make a lot of sense.






Attempt to build the existing microservice micronaut CRUD app to use CQRS:
  
![The diagram above is a rough sketch of an implementation of the CQRS pattern.](https://raw.githubusercontent.com/vahidhedayati/micronaut-vuejs-cqrs/master/docs/diagram-cqrs.png)




References:
> https://microservices.io/patterns/data/cqrs.html

> htps://www.kennybastani.com/2017/01/building-event-driven-microservices.html

> https://www.e4developer.com/2018/03/11/cqrs-a-simple-explanation/


> https://developer.ibm.com/articles/cl-build-app-using-microservices-and-cqrs-trs/ -https://github.com/feliciatucci/cqrs-sample


> https://github.com/jrajani/mini-bank  -- https://www.youtube.com/watch?v=d3Ks40u0tO8

> https://medium.com/@qasimsoomro/building-microservices-using-node-js-with-ddd-cqrs-and-event-sourcing-part-1-of-2-52e0dc3d81df

> https://github.com/mfarache/micronaut-cqrs-kafka

> https://dzone.com/articles/cqrs-by-example-introduction --  https://github.com/asc-lab/java-cqrs-intro

> https://www.youtube.com/watch?v=uTCKzPg0Uak -- https://github.com/sdaschner/scalable-coffee-shop

> https://www.youtube.com/watch?v=JHGkaShoyNs
