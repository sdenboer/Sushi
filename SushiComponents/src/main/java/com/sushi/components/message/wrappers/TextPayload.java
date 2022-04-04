package com.sushi.components.message.wrappers;

public record TextPayload(String text) implements Payload {

    public String getText() {
        return text;
    }
}
