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
public class CancelOrderParam {
    private long ordId;
    private String instId;
    private String userId;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
