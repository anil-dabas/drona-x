package com.spot.exchange.order.util;

import static com.spot.exchange.proto.OrderSide.forNumber;

import com.spot.exchange.order.model.domain.OrderFinish;
import com.spot.exchange.proto.UpdateOrderPayload;
import java.time.ZoneOffset;
import com.spot.exchange.order.model.domain.Order;

public class OrdersUtil {

  public static UpdateOrderPayload createUpdateOrderPayloadFromOrder(Order order) {
    return UpdateOrderPayload.newBuilder().setId(order.getOrderId())
        .setUserId(order.getUserId())
        .setIdStr(String.valueOf(order.getOrderId()))
        .setCreatedAt(order.getTimestamp())
        .setUpdatedAt(order.getUpdatedAt().toEpochSecond(ZoneOffset.UTC) * 1000)
        .setSymbol(order.getInstId())
        .setSide(forNumber(order.getSide()))
        .setPrice(order.getLimitPrice() != 0 ? String.valueOf(order.getLimitPrice()) : "")
        .setQuantity(order.getQuantity())
        .setQuoteQuantity(order.getQuoteQuantity() != null ? order.getQuoteQuantity() : "")
        .setStatus(order.getState().name())
        .setTimeInForce(order.getTimeInForce() !=null ? order.getTimeInForce() : "")
        .setExecutedQuantity(order.getExecutedQuantity())
        .setExecutedQuoteQuantity(order.getExecutedQuoteQuantity() != null ? order.getExecutedQuoteQuantity() : "")
        .build();
  }


  public static OrderFinish createOrderFinishFromOrder(Order order) {
    return OrderFinish.builder()
        .userId(order.getUserId())
        .timestamp(order.getTimestamp())
        .requestId(order.getRequestId())
        .orderId(order.getOrderId())
        .side(order.getSide())
        .orderType(order.getOrderType())
        .quantity(order.getQuantity())
        .limitPrice(order.getLimitPrice())
        .quoteQuantity(order.getQuoteQuantity())
        .instId(order.getInstId())
        .createdAt(order.getCreatedAt())
        .state(order.getState())
        .updatedAt(order.getUpdatedAt())
        .fillPrice(order.getFillPrice())
        .timeInForce(order.getTimeInForce())
        .build();
  }
}
