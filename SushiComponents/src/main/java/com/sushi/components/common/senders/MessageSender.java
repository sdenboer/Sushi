package com.sushi.components.common.senders;

import com.sushi.components.common.OnComplete;
import com.sushi.components.common.message.Message;
import com.sushi.components.common.message.wrappers.HasPayload;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;

public class MessageSender {

    public void send(ByteChannel socketChannel, Message message) throws IOException {
        new TextSender().send(socketChannel, message.toRequest());
        if (message instanceof HasPayload<?> hasPayload) {
            new PayloadSender().send(socketChannel, hasPayload.getPayload());
        }
    }

    public void send(AsynchronousByteChannel socketChannel, Message message) {
        OnComplete sendPayloadOnComplete = null;
        if (message instanceof HasPayload<?> hasPayload) {
            sendPayloadOnComplete = () -> new PayloadSender().send(socketChannel, hasPayload.getPayload(), null);
        }
        new TextSender().send(socketChannel, message.toRequest(), sendPayloadOnComplete);

    }
}
