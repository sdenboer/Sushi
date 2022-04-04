package com.sushi.components.common.senders;

import com.sushi.components.common.OnComplete;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;

public interface Sender<T> {

    void send(ByteChannel socketChannel, T payload) throws IOException;

    void send(AsynchronousByteChannel socketChannel, T payload, OnComplete onComplete);
}
