package com.sushi.components.protocol.order;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface SushiOrderService<T extends SushiOrder> {

    void send(T sushiOrder) throws IOException;


    default void write(SocketChannel channel, SushiOrder sushiOrder) throws IOException {
        String request = sushiOrder.toRequest();
        final ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

}
