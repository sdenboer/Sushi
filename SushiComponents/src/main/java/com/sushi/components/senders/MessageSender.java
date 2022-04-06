package com.sushi.components.senders;

import com.sushi.components.message.Message;
import com.sushi.components.utils.OnComplete;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSender {

    private static final Logger logger = Logger.getLogger(MessageSender.class);

    public static void send(ByteChannel socketChannel, Message message) {
        new TextSender().send(socketChannel, message.toRequest());
        if (message.hasPayload()) {
            new PayloadSender().send(socketChannel, message.getPayloadContext());
        }
    }

    public static void send(AsynchronousByteChannel socketChannel, Message message) {
        OnComplete sendPayloadOnComplete = null;
        logger.info("sending message: " + message.toRequest());
        if (message.hasPayload()) {
            sendPayloadOnComplete = () -> new PayloadSender().send(socketChannel,
                message.getPayloadContext());
        }
        new TextSender().send(socketChannel, message.toRequest(), sendPayloadOnComplete);
    }
}
