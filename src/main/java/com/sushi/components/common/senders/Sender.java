package com.sushi.components.common.senders;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;

public interface Sender<T> {

    void send(SocketChannel socketChannel, T payload);

    void send(AsynchronousSocketChannel socketChannel, T payload);
}