package com.sushi.components.message.wrappers;

public interface HasPayload<T extends Payload> {

    T getPayload();
}
