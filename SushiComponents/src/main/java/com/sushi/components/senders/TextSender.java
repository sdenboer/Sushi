package com.sushi.components.senders;

import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.OnComplete;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import org.apache.log4j.Logger;

public class TextSender implements Sender {

    private static final Logger logger = Logger.getLogger(TextSender.class);

    @Override
    public void send(ByteChannel socketChannel, String payload) {
        final ByteBuffer buffer = ByteBuffer.wrap(payload.getBytes(StandardCharsets.US_ASCII));
        while (buffer.hasRemaining()) {
            try {
                socketChannel.write(buffer);
            } catch (IOException e) {
                logger.error(e);
            }
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
                logger.error(exc);
            }
        });
    }
}
