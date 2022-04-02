package com.sushi.components.common.message.order;

import java.util.Arrays;

public enum SushiOrderMethod {

    STATUS("STATUS"),
    FILE("FILE"),
    PUSH("PUSH"),
    PULL("PULL"),
    REMOVE("REMOVE");

    private final String value;

    SushiOrderMethod(String value) {
        this.value = value;
    }

    public static SushiOrderMethod fromString(String text) {
        return Arrays.stream(SushiOrderMethod.values())
                .filter(value -> value.value.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getValue() {
        return value;
    }
}
