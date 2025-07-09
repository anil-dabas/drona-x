package com.spot.exchange.me.kafka.publisher;

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
}
