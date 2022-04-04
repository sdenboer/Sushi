package com.sushi.components.common.message.order;

import java.util.Arrays;

public enum OrderMethod {

    STATUS("STATUS"),
    FILE("FILE"),
    PUSH("PUSH"),
    PULL("PULL"),
    REMOVE("REMOVE");

    private final String value;

    OrderMethod(String value) {
        this.value = value;
    }

    public static OrderMethod fromString(String text) {
        return Arrays.stream(OrderMethod.values())
                .filter(value -> value.value.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getValue() {
        return value;
    }
}
