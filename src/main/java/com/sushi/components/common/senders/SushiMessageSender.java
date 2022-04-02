package com.sushi.components.common.senders;

import com.sushi.components.common.OnComplete;
import com.sushi.components.common.message.SushiMessage;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.HasSushiWrappers;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;

public class SushiMessageSender implements Sender<SushiMessage> {

    @Override
    public void send(SocketChannel socketChannel, SushiMessage message) {
        if (message instanceof HasSushiWrappers sushiWrappers) {
            new TextSender().send(socketChannel, sushiWrappers.toRequest());
        }
        if (message instanceof HasPayload<?> hasPayload) {
            new PayloadSender().send(socketChannel, hasPayload.getPayload());
        }
    }

    @Override
    public void send(AsynchronousSocketChannel socketChannel, SushiMessage message) {
        OnComplete onComplete = (sushiMessage) -> {
            if (sushiMessage instanceof HasPayload<?> hasPayload) {
                new PayloadSender().send(socketChannel, hasPayload.getPayload());
            }
        };
        if (message instanceof HasSushiWrappers sushiWrappers) {
            new TextSender().send(socketChannel, sushiWrappers.toRequest(), onComplete);
        }

    }
}
