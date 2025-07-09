package com.spot.exchange.order.model.response;

import com.spot.exchange.order.model.domain.OrderState;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {

    private Long id;// "xxxxx",
    private long timestamp;// 1720087432846417648,
    private Long requestId;
    private long orderId;// 2070851335082852352,
    private int side;// "BUY" = 1, // or "SELL -1"
    private int orderType;// "LIMIT", // or "MARKET"
    private String quantity;// "2",
    private BigDecimal limitPrice;// "29",
    private String quoteQuantity;// "0.0",
    private String instId;
    private LocalDateTime createdAt;// 1720087432846417648
    private OrderState state;
    private LocalDateTime updatedAt;
}
