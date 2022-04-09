package com.sushi.components.sender.asynchronous;

import com.sushi.components.sender.SenderStrategy;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.OnComplete;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class AsyncByteChannelTextSender implements SenderStrategy<AsynchronousByteChannel, String> {

    private static final Logger logger = Logger.getLogger(AsyncByteChannelTextSender.class);
    private final OnComplete onComplete;

    public AsyncByteChannelTextSender(OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    public AsyncByteChannelTextSender() {
        this.onComplete = null;
    }

    @Override
    public void send(AsynchronousByteChannel socketChannel, String payload) {
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
