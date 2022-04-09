package com.sushi.components.sender.synchronous;

import com.sushi.components.sender.SenderStrategy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;

public class ByteChannelTextSender implements SenderStrategy<ByteChannel, String> {

    @Override
    public void send(ByteChannel socketChannel, String content) {
        final ByteBuffer buffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.US_ASCII));
        while (buffer.hasRemaining()) {
            try {
                socketChannel.write(buffer);
            } catch (IOException e) {
                System.out.println("Lost connection");
                System.exit(1);
            }
        }
    }

}
