package com.sushi.components.client;

import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class SushiFileOrderService {

    protected final String srcPath;

    protected SushiFileOrderService(String srcPath) {
        this.srcPath = srcPath;
    }

    protected String readServing(SocketChannel socketChannel) throws IOException {
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
