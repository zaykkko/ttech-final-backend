package com.techlab.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    Open(0),
    Confirmed(1),
    Cancelled(2);

    private final int code;
    OrderStatus(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return this.code;
    }

    public static boolean isOpen(OrderStatus status) {
        return OrderStatus.Open.equals(status);
    }

    public static boolean isConfirmed(OrderStatus status) {
        return OrderStatus.Confirmed.equals(status);
    }

    public static boolean isCancelled(OrderStatus status) {
        return OrderStatus.Cancelled.equals(status);
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus e : values()) {
            if (e.getCode() == code) return e;
        }

        throw new IllegalArgumentException("Order status is invalid");
    }
}
