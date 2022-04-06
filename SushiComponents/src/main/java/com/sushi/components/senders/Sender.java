package com.sushi.components.senders;

import com.sushi.components.utils.OnComplete;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;

public interface Sender<T> {

    void send(ByteChannel socketChannel, T payload) throws IOException;

    void send(AsynchronousByteChannel socketChannel, T payload, OnComplete onComplete);
}
