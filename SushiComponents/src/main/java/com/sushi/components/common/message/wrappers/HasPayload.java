package com.sushi.components.common.message.wrappers;

public interface HasPayload<T extends Payload> {

    T getPayload();
}
