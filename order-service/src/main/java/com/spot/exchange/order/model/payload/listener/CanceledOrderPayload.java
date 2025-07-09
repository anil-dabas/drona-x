package com.spot.exchange.order.model.payload.listener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanceledOrderPayload {
  private long orderId;
  private String symbol;
  private long timestamp;

  public static CanceledOrderPayload convertToCanceledOrderPayloadModel(com.spot.exchange.proto.CanceledOrderPayload proto) {
    return CanceledOrderPayload.builder()
        .orderId(proto.getOrderId())
        .symbol(proto.getSymbol())
        .timestamp(proto.getTimestamp())
        .build();
  }
}
