package com.spot.exchange.order.model.payload.publisher;

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
}
