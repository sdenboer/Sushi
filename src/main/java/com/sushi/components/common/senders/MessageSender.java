package com.sushi.components.common.senders;

import com.sushi.components.common.OnComplete;
import com.sushi.components.common.message.Message;
import com.sushi.components.common.message.wrappers.HasPayload;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;

public class MessageSender {

    public void send(SocketChannel socketChannel, Message message) {
        new TextSender().send(socketChannel, message.toRequest());
        if (message instanceof HasPayload<?> hasPayload) {
            new PayloadSender().send(socketChannel, hasPayload.getPayload());
        }
    }

    public void send(AsynchronousSocketChannel socketChannel, Message message) {
        OnComplete sendPayloadOnComplete = null;
        if (message instanceof HasPayload<?> hasPayload) {
            sendPayloadOnComplete = () -> new PayloadSender().send(socketChannel, hasPayload.getPayload(), null);
        }
        new TextSender().send(socketChannel, message.toRequest(), sendPayloadOnComplete);

    }
}
