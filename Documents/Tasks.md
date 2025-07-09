# drona-x (DX)

## Task Tracking 
- I will be tracking the tasks using This MD file 
- The tasks will be named as DX-XXX where XXX represents the number of the task 

### DX-001 Initial commit - Completed
- This is the initial commit of the code 

### DX-002 Redis update and refining RingBuffer - Partially Completed
- This task is to start using redis on ODX and refining the RingBuffer size
- Redis Implementation for saving order - completed 
- RingBuffer resizing - Ongoing current buffer size is 130K

### DX-003 Consuming the processed orders from matching engine and updating redis - In Progress
- Consume the matched orders from ```matching.order.matched``` kafka topic - completed
- Cancelled orders from ```matching.order.canceled``` kafka - completed
- Updating the order status on Redis - completed

### DX-004 Making the matching engine multi-market - In-Progress 


### DX-005 Making the quantity and the price values to be decimal rather than integer values - Upcoming
- Converting the price to BigDecimal 
- Converting quantity to floating point 

### DX-006 Adding order finish table and moving the finished orders to the order-finish table - Done
- Creating ORDER_FINISH table and moving all the finished orders to this table 
- marking the orders status to PENDING, PARTIALLY FILLED and FILLED status 

### DX-007 Adding the deployment script to deploy the stack - Partially finished 
- Matching-engine the Order-service and the driver to a EC-2 instance 
- Adding other stack such as Kafka, Redis and MySQL as well 

### DX-008 Enhancing the Matching engine throughput - Achieved 400K
- currently able to achieve approx 160K target for 450K
- Removing String Parsing 
- simplifying remove empty order logic 
- Consumer and Producer fastening
- Using Protobuf - completed 


### DX-009 Optimising Order-service for efficient connections and user handling - In progress

### DX-010 Removing the hardcoded user and implementing memory user 

### DX-011 Reorganizing the whole project and moving all the models to protobuf idea - In Progress


## Bugs Tracking

### DX-BUG-001 The trade price for the matching is not correct 
- Description : Need to check the trade price for the trade as it is not correct
- Reported by : Paymen 
