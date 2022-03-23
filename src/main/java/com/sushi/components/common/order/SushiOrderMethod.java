package com.sushi.components.common.order;

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

    public String getValue() {
        return value;
    }

    public static SushiOrderMethod fromString(String text) {
        return Arrays.stream(SushiOrderMethod.values())
                .filter(value -> value.value.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
