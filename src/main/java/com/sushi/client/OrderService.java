package com.sushi.client;

import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;

public interface OrderService {

    Serving send(ByteChannel byteChannel, Order order) throws IOException;

    default String receiveServing(ByteChannel socketChannel) throws IOException {
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


    default String receiveTextPayload(ByteChannel socketChannel, int payloadSize) throws IOException {
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
