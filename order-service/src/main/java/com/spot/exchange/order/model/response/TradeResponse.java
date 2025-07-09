package com.spot.exchange.order.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse {

    private long id;
    private String symbol;
    private long buy;
    private long sell;
    private String quantity;
    private String price;
    @JsonProperty("isBuyerMaker")
    private boolean buyerMaker;
    private long timestamp;

    public static TradeResponse convertToTradeResponseModel(com.spot.exchange.proto.TradeResponse proto) {
        return TradeResponse.builder()
            .id(proto.getId())
            .symbol(proto.getSymbol())
            .buy(proto.getBuy())
            .sell(proto.getSell())
            .quantity(String.valueOf(proto.getQuantity()))
            .price(String.valueOf(proto.getPrice()))
            .buyerMaker(proto.getIsBuyerMaker())
            .timestamp(proto.getTimestamp())
            .build();
    }
}
