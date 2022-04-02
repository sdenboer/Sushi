package com.sushi.components.client;

import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface SushiOrderService<T extends SushiOrder, E extends SushiServing> {

    E send(T sushiOrder);


    default void write(SocketChannel channel, SushiOrder sushiOrder) throws IOException {
        String request = sushiOrder.toRequest();
        final ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    default String readServing(SocketChannel socketChannel) throws IOException {
        StringBuilder response = new StringBuilder();

        while (!response.toString().contains("status")) {
            final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
            final long bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                response.append(new String(buffer.array()));
            }
        }
        return response.toString();
    }

}
