## How to test the Matching engine 

### Background 

- This document explains how we can test the Matching engine for its throughput for matching operation 
- If we see there are three submodules in the project 
  1. Driver Service—Mimic the Users who are sending the orders
  2. Order Service  - Acts as the ODX
  3. Matching Engine - Actual matching engine 

### Flow to test 

1. Spin the Redis, Kafka and MySQL DB using ```docker-compose -f docker-compose.dependencies.yml up -d --remove-orphans ``` 
2. Now Run the ```order-service``` application 
3. Once order-service is fully up Run ```driver``` service and wait for the log ```Finished executing XXXX order in YYY Milliseconds```
    for example if you run with current config you will get ```Finished executing 1404000 order in AAA Milliseconds and BBB Seconds .```
4. When you finally see the above log then run the ```matching-engine```