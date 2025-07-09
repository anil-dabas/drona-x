package com.spot.exchange.order.service;

import static com.spot.exchange.order.cache.ListedPairsCache.listedPairs;
import static com.spot.exchange.order.util.Constants.ORDER_CACHE_PREFIX;
import static com.spot.exchange.order.util.Constants.ORDER_FINISH_CACHE_PREFIX;
import static com.spot.exchange.order.util.OrdersUtil.createOrderFinishFromOrder;
import static com.spot.exchange.order.util.OrdersUtil.createUpdateOrderPayloadFromOrder;
import static com.spot.exchange.order.util.TradeUtil.convertMicrosecondsToLocalDateTime;
import static com.spot.exchange.order.util.TradeUtil.convertToMicroseconds;
import static com.spot.exchange.order.util.TradeUtil.toBigDecimal;
import static com.spot.exchange.order.util.TradeUtil.toStringFromBigDecimal;

import com.spot.exchange.order.kafka.KafkaOrderPublisher;
import com.spot.exchange.order.model.domain.Order;
import com.spot.exchange.order.model.domain.OrderAction;
import com.spot.exchange.order.model.domain.OrderFinish;
import com.spot.exchange.order.model.domain.OrderState;
import com.spot.exchange.order.model.payload.listener.CanceledOrderPayload;
import com.spot.exchange.order.model.request.PlaceOrderParam;
import com.spot.exchange.order.model.response.TradeResponse;
import com.spot.exchange.order.repo.OrderFinishRepository;
import com.spot.exchange.order.repo.OrderRepository;
import com.spot.exchange.proto.OrderRequestPayload;
import com.spot.exchange.proto.UpdateOrderPayload;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  KafkaOrderPublisher orderPublisher;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  OrderFinishRepository orderFinishRepository;

  private final RedisTemplate<String, Order> orderRedisTemplate;
  private final RedisTemplate<String, OrderFinish> orderFinishRedisTemplate;

  @Autowired
  public OrderServiceImpl(RedisTemplate<String, Order> orderRedisTemplate,RedisTemplate<String, OrderFinish> orderFinishRedisTemplate) {
    this.orderRedisTemplate = orderRedisTemplate;
    this.orderFinishRedisTemplate = orderFinishRedisTemplate;
    log.info("Injected RedisTemplate: {}", orderRedisTemplate);
    log.info("Value serializer: {}", orderRedisTemplate.getValueSerializer());
  }

  @Override
  public void submitOrderRequestToQueue(Order order, OrderAction action) {
    // Need to map properly
    OrderRequestPayload payload = OrderRequestPayload.newBuilder().setId(order.getRequestId().toString())
        .setOrderAction(action.getValue()).setOrderType(order.getOrderType()).setOrderId(order.getOrderId())
        .setSide(order.getSide()).setQuantity(Integer.parseInt(order.getQuantity()))
        .setCreatedAt(convertToMicroseconds(order.getCreatedAt()))
        .setPrice(Integer.parseInt(String.valueOf(order.getLimitPrice()))).build();
    log.info("Publish to kafka : Instrument id {} OrderPayload {} ", order.getInstId(), payload);
    orderPublisher.publishOrder(payload);
  }

  @Override
  public boolean validatePlaceOrderParams(PlaceOrderParam placeOrderParam) {
    return listedPairs.contains(placeOrderParam.getInstId());
  }

  @Override
  public void updateMatchedOrderDetailsAndPublishResponse(TradeResponse tradeResponse) {
    // Finding Order details involved in the trade.
    log.info("Match Order : The trade response received is  {}", tradeResponse);
    Order buyOrder = getOrderFromCacheOrDatabase(tradeResponse.getBuy());
    Order sellOrder = getOrderFromCacheOrDatabase(tradeResponse.getSell());
    log.info("Match Order : The buy order matched is {} & Sell order matched is {}", buyOrder,
        sellOrder);

    LocalDateTime timeOfExecution = convertMicrosecondsToLocalDateTime(
        tradeResponse.getTimestamp());

    // Updating buy Order
    buyOrder.setUpdatedAt(timeOfExecution);
    BigDecimal updatedBuyExecuted = toBigDecimal(buyOrder.getExecutedQuantity()).add(toBigDecimal(tradeResponse.getQuantity()));
    buyOrder.setExecutedQuantity(toStringFromBigDecimal(updatedBuyExecuted));
    buyOrder.setState(OrderState.PARTIALLY_FILLED);

    // Updating sell Order
    sellOrder.setUpdatedAt(timeOfExecution);
    BigDecimal updatedSellExecuted = toBigDecimal(sellOrder.getExecutedQuantity()).add(toBigDecimal(tradeResponse.getQuantity()));
    sellOrder.setExecutedQuantity(toStringFromBigDecimal(updatedSellExecuted));
    sellOrder.setState(OrderState.PARTIALLY_FILLED);

    log.info("Match Order : Saving updated orders in Redis and database");
    saveOrderToCache(buyOrder);
    saveOrderToCache(sellOrder);
    orderRepository.saveAll(List.of(buyOrder, sellOrder));

    // Save Finished Orders
    if(toBigDecimal(buyOrder.getQuantity()).compareTo(toBigDecimal(buyOrder.getExecutedQuantity())) ==0){
      createAndSaveFinishedOrder(buyOrder);
    }
    if(toBigDecimal(sellOrder.getQuantity()).compareTo(toBigDecimal(sellOrder.getExecutedQuantity())) ==0){
      createAndSaveFinishedOrder(sellOrder);
    }

    // Convert to UpdatedOrderPayload Buy and Sell Order
    UpdateOrderPayload updateBuyOrderPayload = createUpdateOrderPayloadFromOrder(buyOrder);
    UpdateOrderPayload updateSellOrderPayload = createUpdateOrderPayloadFromOrder(sellOrder);

    log.info("Match Order : Publishing buy order to update order kafka {}", updateBuyOrderPayload);
    orderPublisher.publishOrderUpdate(updateBuyOrderPayload);
    log.info("Match Order : Publishing sell order to update order kafka {}", updateSellOrderPayload);
    orderPublisher.publishOrderUpdate(updateSellOrderPayload);

  }

  private void createAndSaveFinishedOrder(Order order) {
    order.setState(OrderState.FILLED);
    OrderFinish orderFinish = createOrderFinishFromOrder(order);
    log.info("Match Order : Saving in finished order table");
    // Save to both cache and database
    saveOrderFinishToCache(orderFinish);
    orderFinishRepository.save(orderFinish);

    // Update the original order in database and remove from active orders cache
    orderRepository.save(order);
    deleteOrderFromCache(order.getOrderId());
  }

  @Override
  public void updateCanceledOrderDetailsAndPublishResponse(CanceledOrderPayload canceledOrderPayload) {
    Order canceledOrder = getOrderFromCacheOrDatabase(canceledOrderPayload.getOrderId());
    canceledOrder.setUpdatedAt(convertMicrosecondsToLocalDateTime(canceledOrderPayload.getTimestamp()));
    canceledOrder.setState(OrderState.CANCELLED);

    log.info("Cancel Order : Saving canceled orders in Redis and database");
    saveOrderToCache(canceledOrder);
    orderRepository.save(canceledOrder);

    com.spot.exchange.proto.UpdateOrderPayload updateCanceledOrderPayload = createUpdateOrderPayloadFromOrder(canceledOrder);
    log.info("Cancel Order : Publishing canceled order to update order kafka {}", updateCanceledOrderPayload);
    orderPublisher.publishOrderUpdate(updateCanceledOrderPayload);
  }

  @Override
  public Order getOrderFromCacheOrDatabase(long orderId) {
    // Try to fetch the order from Redis
    String cacheKey = ORDER_CACHE_PREFIX + orderId;
    Order order = orderRedisTemplate.opsForValue().get(cacheKey);

    if (order != null) {
      log.info("Order {} fetched from Redis cache.", orderId);
      return order;
    }
    // If not found in Redis, fetch from MySQL
    order = orderRepository.findByOrderId(orderId);
    if (order != null) {
      // Save the order to Redis for future use
      saveOrderToCache(order);
      log.info("Order {} fetched from MySQL and saved to Redis cache.", orderId);
    }
    return order;
  }
  @Override
  public void saveOrderToCache(Order order) {
    String cacheKey = ORDER_CACHE_PREFIX + order.getOrderId();
    orderRedisTemplate.opsForValue().set(cacheKey, order, 2, TimeUnit.HOURS);
    log.info("Order {} saved to Redis cache.", order.getOrderId());
  }

  @Override
  public void saveOrderFinishToCache(OrderFinish orderFinish) {
    String cacheKey = ORDER_FINISH_CACHE_PREFIX + orderFinish.getOrderId();
    orderFinishRedisTemplate.opsForValue().set(cacheKey, orderFinish, 2, TimeUnit.HOURS);
    log.info("OrderFinish {} saved to Redis cache.", orderFinish.getOrderId());
  }

  @Override
  public void deleteOrderFromCache(long orderId) {
    String cacheKey = ORDER_CACHE_PREFIX + orderId;
    Boolean deleted = orderRedisTemplate.delete(cacheKey);
    if (deleted != null && deleted) {
      log.info("Order {} removed from Redis cache.", orderId);
    } else {
      log.info("Order {} not found in Redis cache or already removed.", orderId);
    }
  }
}
