package com.sushi.components.common.message.wrappers;

import java.nio.charset.StandardCharsets;

public record TextPayload(String text) implements Payload {

    public String getText() {
        return text;
    }
}
