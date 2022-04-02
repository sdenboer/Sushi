package com.sushi.components.common.senders;

import com.sushi.components.common.OnComplete;
import com.sushi.components.utils.ChannelUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TextSender implements Sender<String> {

    @Override
    public void send(SocketChannel socketChannel, String payload) {
        final ByteBuffer buffer = ByteBuffer.wrap(payload.getBytes());
        while (buffer.hasRemaining()) {
            try {
                socketChannel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(AsynchronousSocketChannel socketChannel, String payload, OnComplete onComplete) {
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
