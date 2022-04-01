package com.sushi.components.common.serving;

import java.util.Arrays;

public enum SushiServingWrapperField {

    STATUS("status"),
    CONTENT("content"),
    ENCRYPTION("encryption"),
    ORDER_ID("orderId"),
    FILE_SIZE("file-size");

    private final String value;

    SushiServingWrapperField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SushiServingWrapperField fromString(String text) {
        return Arrays.stream(SushiServingWrapperField.values())
                .filter(value -> value.value.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
