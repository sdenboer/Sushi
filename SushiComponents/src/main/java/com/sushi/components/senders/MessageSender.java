package com.sushi.components.senders;

import com.sushi.components.utils.OnComplete;
import com.sushi.components.message.Message;
import com.sushi.components.message.wrappers.HasPayload;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;

public class MessageSender {

    private static final Logger logger = Logger.getLogger(MessageSender.class);

    public void send(ByteChannel socketChannel, Message message) throws IOException {
        new TextSender().send(socketChannel, message.toRequest());
        if (message instanceof HasPayload<?> hasPayload) {
            new PayloadSender().send(socketChannel, hasPayload.getPayload());
        }
    }

    public void send(AsynchronousByteChannel socketChannel, Message message) {
        OnComplete sendPayloadOnComplete = null;
        logger.info("sending message: " + message.toRequest());
        if (message instanceof HasPayload<?> hasPayload) {
            sendPayloadOnComplete = () -> new PayloadSender().send(socketChannel, hasPayload.getPayload(), null);
        }
        new TextSender().send(socketChannel, message.toRequest(), sendPayloadOnComplete);

    }
}
