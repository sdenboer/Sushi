package com.sushi.components.client;

import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.serving.SushiServing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface SushiOrderService<T extends SushiOrder, E extends SushiServing> {

    E send(T sushiOrder) throws IOException;


    default void write(SocketChannel channel, SushiOrder sushiOrder) throws IOException {
        String request = sushiOrder.toRequest();
        final ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

}
