## Running the matching engine with tuning 

1. Run order-service 
2. Run driver-service
3. Once the driver service finished you will get the number of total orders that are submitted. take a number around 80% of that value 
    for ex if the number is 1,500,000 then take 80% = 1,200,000 as the number of orders submitted 
4. Open ```matching-engine``` ```OrderHandller.java``` and provide the expectedOrderCount as this value
5. build the ```matching-engine```
6. go to the jar file for matching-engine in path ```root/matching-engine/build/libs``` and run below command to run the matching engine 
    ```
   java -server -Xmx64g -Xms64g \
     -XX:+UseZGC -XX:+ZGenerational \
     -XX:MaxGCPauseMillis=1 \
     -XX:ParallelGCThreads=14 \
     -XX:ConcGCThreads=7 \
     -XX:+UseLargePages \
     -XX:+PerfDisableSharedMem \
     -XX:+DisableExplicitGC \
     -XX:+AlwaysPreTouch \
     -Djava.net.preferIPv4Stack=true \
     -Djdk.nio.maxCachedBufferSize=262144 \
     -jar matching-engine.jar
   ```
7. once the order matching has reached the expected count it will give you a log saying how much time it took to match and wts the rate of matching
    ```2025-04-21T20:45:09.776+04:00  INFO 15884 --- [pool-2-thread-1] c.spot.exchange.me.engine.OrderHandler   : Processed 3000000 orders in 11948.914333 ms (251068 ops/sec)```

# drona-x
    ```
    
    java -server -Xmx30g -Xms30g \
    -XX:+UseZGC -XX:+ZGenerational \
    -XX:MaxGCPauseMillis=1 \
    -XX:ParallelGCThreads=10 \
    -XX:ConcGCThreads=5 \
    -XX:+PerfDisableSharedMem \
    -XX:+DisableExplicitGC \
    -Djava.net.preferIPv4Stack=true \
    -jar matching-engine.jar
    
    ```
