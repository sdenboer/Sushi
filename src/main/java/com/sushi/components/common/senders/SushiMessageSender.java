package com.sushi.components.common.senders;

import com.sushi.components.common.OnComplete;
import com.sushi.components.common.message.SushiMessage;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.HasSushiWrappers;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;

public class SushiMessageSender {

    public void send(SocketChannel socketChannel, SushiMessage message) {
        if (message instanceof HasSushiWrappers sushiWrappers) {
            new TextSender().send(socketChannel, sushiWrappers.toRequest());
        }
        if (message instanceof HasPayload<?> hasPayload) {
            new PayloadSender().send(socketChannel, hasPayload.getPayload());
        }
    }

    public void send(AsynchronousSocketChannel socketChannel, SushiMessage message) {
        if (message instanceof HasSushiWrappers sushiWrappers) {
            OnComplete sendPayloadOnComplete = null;
            if (message instanceof HasPayload<?> hasPayload) {
                sendPayloadOnComplete = () -> new PayloadSender().send(socketChannel, hasPayload.getPayload(), null);
            }
            new TextSender().send(socketChannel, sushiWrappers.toRequest(), sendPayloadOnComplete);
        }

    }
}
