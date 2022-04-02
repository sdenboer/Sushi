package com.sushi.components.server;

import com.sushi.components.common.senders.PayloadSender;
import com.sushi.components.common.message.serving.SushiServing;
import com.sushi.components.common.senders.SushiMessageSender;
import com.sushi.components.utils.ChannelUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ServingService {

    private final AsynchronousSocketChannel socketChannel;
    private final SushiServing serving;

    public ServingService(AsynchronousSocketChannel socketChannel, SushiServing serving) {
        this.socketChannel = socketChannel;
        this.serving = serving;
    }

    public void send() {
        final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {

            @Override
            public void completed(final Integer result, final Void attachment) {
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer, attachment, this);
                }
                new SushiMessageSender().send(socketChannel, serving);
                if (Objects.isNull(serving.getPayload())) {
                    ChannelUtils.close(socketChannel);
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
