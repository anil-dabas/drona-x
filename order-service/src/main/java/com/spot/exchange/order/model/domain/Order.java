package com.spot.exchange.order.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity(name = "order")
@Table(name = "ex_order")
public class Order {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;// "xxxxx",
       private String userId;
       private long timestamp;// 1720087432846417648,
       private Long requestId;
       private long orderId;// 2070851335082852352,
       private int side;// "BUY" = 1, // or "SELL -1"
       private int orderType;// "LIMIT", // or "MARKET"
       private String quantity;// "2",
       private int limitPrice;// "29",
       private String quoteQuantity;// "0.0",
       private String instId;
       private LocalDateTime createdAt;
       @Enumerated(EnumType.ORDINAL)// 1720087432846417648
       private OrderState state;
       private LocalDateTime updatedAt;
       private int fillPrice;
       private String timeInForce; // "GTC",
       private String executedQuantity;    // "0",
       private String executedQuoteQuantity;   // "0"

}
