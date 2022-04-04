package com.sushi.components.common.message.wrappers;

public record TextPayload(String text) implements Payload {

    public String getText() {
        return text;
    }
}
