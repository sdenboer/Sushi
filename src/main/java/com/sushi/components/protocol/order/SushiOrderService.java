package com.sushi.components.protocol.order;

import com.sushi.components.protocol.Response;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface SushiOrderService<T extends SushiOrder> {

    Response send(T sushiOrder) throws IOException;


    default void write(SocketChannel channel, SushiOrder sushiOrder) throws IOException {
        String request = sushiOrder.toRequest();
        final ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

}
