Micronaut backend vuejs frontend application - CQRS
---


###### Youtube [Video demonstrating product part 2 - Latest as per diagram](https://www.youtube.com/watch?v=SB2JP6aF5Fs). Written [description available here](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/detailedDescription.md).

###### Youtube [Video demonstrating product part 1 - older video](https://www.youtube.com/watch?v=-pKr6Zg-MtA). Relates to [websocket-1 branch git clone https://github.com/vahidhedayati/micronaut-vuejs-cqrs -b websocket-v1](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/tree/websocket-v1) and [websocket-2 branch git clone https://github.com/vahidhedayati/micronaut-vuejs-cqrs -b websocket-v2](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/tree/websocket-v2)


![how this app works](https://raw.githubusercontent.com/vahidhedayati/micronaut-vuejs-cqrs/master/docs/eventstoreCQRS-latest.png)



Running app
----
##### Please refer to [pre-requirements](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/configure.md).

```
./gradlew hotel-read:run  userbase-read:run frontend:start gateway-command:run gateway-query:run hotel-write:run  userbase-write:run  --parallel
```

##### Starting apps from 3 terminals :
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



The above will launch 1 instance of frontend vuejs site running on `localhost:3000` 
and a backend micronaut site running on port `localhost:{random}` a gateway micronaut app running on port 
`localhost:8080` 



##### Please refer to [what CQRS is here](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/cqrs-explained.md).

