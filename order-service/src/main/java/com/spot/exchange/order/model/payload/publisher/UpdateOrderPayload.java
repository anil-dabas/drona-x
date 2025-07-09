package com.spot.exchange.order.model.payload.publisher;

import com.spot.exchange.order.model.domain.OrderSide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderPayload {
    private Long id;    // 2072271355033796608,
    private String userId;   //"123456",
    private String idStr;   // "2072271355033796608",
    private Long createdAt; // 1720086389349268000,
    private Long updatedAt;    // 1720086389349268000,
    private String symbol;  // "BTC_USDT",
    private OrderSide side; // "BUY",
    private String price;   // "50",
    private String quantity;    // "1",
    private String quoteQuantity;   // "0",
    private String status;  // "NEW",
    private String timeInForce; // "GTC",
    private String executedQuantity;    // "0",
    private String executedQuoteQuantity;   // "0"
}
