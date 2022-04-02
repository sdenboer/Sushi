package com.sushi.components.common.message.wrappers;

import java.util.Arrays;

public enum SushiWrapperField {
    METHOD("method"),
    HOST("host"),
    PORT("port"),
    ORDER_ID("order-id"),
    FILE("file"),
    DIR("dir"),
    CONTENT_LENGTH("content-length"),
    ENCRYPTION("encryption"),
    CONTENT("content"),
    STATUS("status");

    private final String field;

    SushiWrapperField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static SushiWrapperField fromString(String text) {
        return Arrays.stream(SushiWrapperField.values())
                .filter(value -> value.field.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }


}
