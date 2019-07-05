Event driven CQRS application - Written in Micronaut backend, vuejs frontend 
------------------


--------------

Running app
----
##### Please refer to [pre-requirements](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/configure.md).

--------

###### Please allow top 2 terminals to start delay 3rd gateway-command by around 15 seconds until terminals 1 + 2 apps are up.

```
# Terminal 1
#  node process hangs on which also keeps jvms active - killing node kills all other jvms hanging off
kill -9 $(netstat -pln 2>/dev/null |grep LISTEN|grep node|awk '{print $7}'|awk -F"/" '{print $1}');
./gradlew  userbase-read:run frontend:start  gateway-query:run  userbase-write:run   --parallel
#terminal 2
./gradlew hotel-read:run  hotel-write:run  --parallel
#terminal 3 
./gradlew gateway-command:run
```


---------------------

![how this app works](https://raw.githubusercontent.com/vahidhedayati/micronaut-vuejs-cqrs/master/docs/eventstoreCQRS-latest.png)

---------------------


###### Youtube [Video demonstrating product part 2 - Latest as per diagram](https://www.youtube.com/watch?v=SB2JP6aF5Fs). Written [description available here](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/detailedDescription.md).


---------------------

The above will launch 1 instance of :
> frontend vuejs site running on `localhost:3000`

> gateway-query port  `localhost:8081` 

> gateway-command port  `localhost:8082` 

> hotel-read port  `localhost:{random}` 
 
> hotel-write port  `localhost:{random}`

> user-read port  `localhost:{random}`  

> user-write port  `localhost:{random}`


--------------

###### Youtube [Video demonstrating product part 1 - older video](https://www.youtube.com/watch?v=-pKr6Zg-MtA).
Part 1  relates to:
 
> [websocket-1 branch git clone https://github.com/vahidhedayati/micronaut-vuejs-cqrs -b websocket-v1]
(https://github.com/vahidhedayati/micronaut-vuejs-cqrs/tree/websocket-v1) 
and 

> [websocket-2 branch git clone https://github.com/vahidhedayati/micronaut-vuejs-cqrs -b websocket-v2](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/tree/websocket-v2)

--------------


##### CQRS further reading, please refer to [what CQRS is here](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/cqrs-explained.md).

