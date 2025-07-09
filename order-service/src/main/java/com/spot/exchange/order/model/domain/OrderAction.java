package com.spot.exchange.order.model.domain;

import lombok.Getter;

@Getter
public enum OrderAction {

    CANCEL_ORDER(1),
    PLACE_ORDER(2);

    private final int value;

    OrderAction(int value) {
        this.value = value;
    }
}
