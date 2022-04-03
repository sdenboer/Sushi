package com.sushi.components.client;

import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public interface OrderService<T extends Order, E extends Serving> {

    E send(T order);

    default String receiveServing(SocketChannel socketChannel) throws IOException {
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


    default String receiveTextPayload(SocketChannel socketChannel, int payloadSize) throws IOException {
        StringBuilder response = new StringBuilder();
        while (response.toString().getBytes(StandardCharsets.UTF_8).length < payloadSize) {
            final ByteBuffer buffer = ByteBuffer.allocate(payloadSize);
            final long bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                response.append(new String(buffer.array()));
            }
        }
        return response.toString();
    }


}
