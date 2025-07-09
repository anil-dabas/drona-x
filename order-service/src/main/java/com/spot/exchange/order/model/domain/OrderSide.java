package com.spot.exchange.order.model.domain;

import lombok.Getter;

@Getter
public enum OrderSide {
    BUY(1),
    SELL(-1);

    private final int value;

    OrderSide(int value) {
        this.value = value;
    }

    public static OrderSide fromValue(int value) {
        for (OrderSide side : OrderSide.values()) {
            if (side.getValue() == value) {
                return side;
            }
        }
        throw new IllegalArgumentException("Unknown OrderSide value: " + value);
    }

}
