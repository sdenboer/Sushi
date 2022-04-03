package com.sushi.components.common.message.wrappers;

import java.util.Arrays;

public enum WrapperField {
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

    WrapperField(String field) {
        this.field = field;
    }

    public static WrapperField fromString(String text) {
        return Arrays.stream(WrapperField.values())
                .filter(value -> value.field.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getField() {
        return field;
    }


}
