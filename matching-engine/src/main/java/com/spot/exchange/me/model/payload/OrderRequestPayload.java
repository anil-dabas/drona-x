package com.spot.exchange.me.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestPayload {

  private String id;// "xxxxx",
  private int orderAction;// 1- cancel Order, 2 - Place Order, 3 - Amend -order
  private long timestamp;// 1720087432846417648,
  private long orderId;// 2070851335082852352,
  private int side;// "BUY", // or "SELL"
  private int orderType;// "LIMIT", // or "MARKET"
  private int quantity;// "2",
  private int price;// "29",
  private long createdAt;

  public static OrderRequestPayload convertToOrderRequestPayloadModel(
      com.spot.exchange.proto.OrderRequestPayload proto) {
    return OrderRequestPayload.builder()
        .id(proto.getId())
        .orderAction(proto.getOrderAction())
        .timestamp(proto.getTimestamp())
        .orderId(proto.getOrderId())
        .side(proto.getSide())
        .orderType(proto.getOrderType())
        .quantity(proto.getQuantity())
        .price(proto.getPrice())
        .createdAt(proto.getCreatedAt())
        .build();
  }
}
