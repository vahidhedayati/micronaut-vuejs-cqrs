Micronaut backend vuejs frontend application - CQRS
---

Attempt to build the existing microservice micronaut CRUD app to use CQRS: Image taken from: Taken from: https://www.kennybastani.com/2017/01/building-event-driven-microservices.html
  
![The diagram above is a rough sketch of an implementation of the CQRS pattern.](https://raw.githubusercontent.com/vahidhedayati/micronaut-vuejs-cqrs/master/docs/cqrs.png)

https://microservices.io/patterns/data/cqrs.html
![The diagram above is a rough sketch of an implementation of the CQRS pattern.](https://raw.githubusercontent.com/vahidhedayati/micronaut-vuejs-cqrs/master/docs/QuerySideService.png)





Running app
----

##### Run Consul

To run first either install consul locally and run `./consul agent dev`

or if you have installed docker simply run `sudo docker run -p 8500:8500 consul`
 
 
 

##### To start all applications in one session run:

```
./gradlew frontend:start backend:run gateway:run userbase:run --parallel


# Advanced: 
# When running on linux a process for node hangs on which also keeps jvms active - killing node kills all other jvms hanging off
# this is all in 1 line to kill if found and start apps

kill -9 $(netstat -pln 2>/dev/null |grep LISTEN|grep node|awk '{print $7}'|awk -F"/" '{print $1}'); ./gradlew frontend:start backend:run gateway:run userbase:run --parallel



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
