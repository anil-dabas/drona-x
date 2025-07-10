## Running the matching engine with tuning 

1. Run order-service 
2. Run driver-service
3. Once the driver service finished you will get the number of total orders that are submitted. take a number around 80% of that value 
    for ex if the number is 1,500,000 then take 80% = 1,200,000 as the number of orders submitted 
4. Open ```matching-engine``` ```OrderHandller.java``` and provide the expectedOrderCount as this value
5. build the ```matching-engine```
6. go to the jar file for matching-engine in path ```root/matching-engine/build/libs``` and run below command to run the matching engine 
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