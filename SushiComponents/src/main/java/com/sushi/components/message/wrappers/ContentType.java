package com.sushi.components.message.wrappers;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ContentType {

    TXT("txt"),
    FILE("file");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static ContentType fromString(String text) {
        return Arrays.stream(ContentType.values())
                .filter(value -> value.type.equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }
}
