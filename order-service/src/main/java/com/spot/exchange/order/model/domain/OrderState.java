package com.spot.exchange.order.model.domain;

import lombok.Getter;

@Getter
public enum OrderState {
    PLACED(0),
    PENDING(1),
    PARTIALLY_FILLED(2),
    FILLED(3),
    CANCELLED(4),
    INVALID(-1); // Add an INVALID state

    private final int value;

    OrderState(int value) {
        this.value = value;
    }

    // Static method to get OrderState by value
    public static OrderState fromValue(int value) {
        for (OrderState state : OrderState.values()) {
            if (state.value == value) {
                return state;
            }
        }
        // Return INVALID if no matching state is found
        return INVALID;
    }
}