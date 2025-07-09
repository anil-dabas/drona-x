package com.spot.exchange.order.model.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderParam {

    private String instId;
    private int side;
    private String quantity;
    // Only for limit orders
    private String limitPrice;
    private String userId;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
