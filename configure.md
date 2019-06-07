
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

zookeeper-server-start.bat  c:\dev\kafka\config\zookeeper.properties


# open cmd and run (cmd3)

kafka-server-start.bat c:\dev\kafka\config\server.properties


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