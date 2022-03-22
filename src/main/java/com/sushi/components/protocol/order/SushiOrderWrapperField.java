package com.sushi.components.protocol.order;

import java.util.Arrays;

public enum SushiOrderWrapperField {
    METHOD("method"),
    HOST("host"),
    PORT("port"),
    ORDER_ID("order_id"),
    FILE("file"),
    DIR("dir"),
    FILE_SIZE("file-size"),
    ENCRYPTION("encryption"),
    CONTENT("content");

    private final String value;

    SushiOrderWrapperField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SushiOrderWrapperField fromString(String text) {
        return Arrays.stream(SushiOrderWrapperField.values())
                .filter(value -> value.value.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }


}
