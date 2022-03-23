package com.sushi.components.server;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.utils.ChannelUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class TextServingResponse implements ServingService {

    private final AsynchronousSocketChannel socketChannel;

    public TextServingResponse(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void sendServing(SushiServing serving) {
        final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {

            @Override
            public void completed(final Integer result, final Void attachment) {
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer, attachment, this);
                }
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
                ChannelUtils.close(socketChannel);
                throw new RuntimeException("unable to confirm", exc);
            }
        });
    }

}
