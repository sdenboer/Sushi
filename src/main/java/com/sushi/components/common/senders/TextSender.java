package com.sushi.components.common.senders;

import com.sushi.components.common.OnComplete;
import com.sushi.components.utils.ChannelUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class TextSender implements Sender<String> {

    @Override
    public void send(ByteChannel socketChannel, String payload) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(payload.getBytes(StandardCharsets.US_ASCII));
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    @Override
    public void send(AsynchronousByteChannel socketChannel, String payload, OnComplete onComplete) {
        final ByteBuffer payloadBuffer = ByteBuffer.wrap(payload.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(payloadBuffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                while (payloadBuffer.hasRemaining()) {
                    socketChannel.write(payloadBuffer, attachment, this);
                }
                if (onComplete != null) {
                    onComplete.onComplete();
                } else {
                    ChannelUtils.close(socketChannel);
                }


            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });
    }
}
