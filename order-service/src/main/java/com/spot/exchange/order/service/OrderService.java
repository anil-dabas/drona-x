package com.spot.exchange.order.service;

import com.spot.exchange.order.model.domain.Order;
import com.spot.exchange.order.model.domain.OrderAction;
import com.spot.exchange.order.model.domain.OrderFinish;
import com.spot.exchange.order.model.payload.listener.CanceledOrderPayload;
import com.spot.exchange.order.model.request.PlaceOrderParam;
import com.spot.exchange.order.model.response.TradeResponse;

public interface OrderService {

  void submitOrderRequestToQueue(Order order, OrderAction action);

  boolean validatePlaceOrderParams(PlaceOrderParam placeOrderParam);

  void updateMatchedOrderDetailsAndPublishResponse(TradeResponse tradeResponse);

  void updateCanceledOrderDetailsAndPublishResponse(CanceledOrderPayload canceledOrderPayload);

  Order getOrderFromCacheOrDatabase(long orderId);

  void saveOrderToCache(Order order);

  void saveOrderFinishToCache(OrderFinish orderFinish);

  void deleteOrderFromCache(long orderId);
}
