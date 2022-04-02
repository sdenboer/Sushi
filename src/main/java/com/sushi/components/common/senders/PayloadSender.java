package com.sushi.components.common.senders;

import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.message.wrappers.Payload;
import com.sushi.components.common.message.wrappers.TextPayload;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;

public class PayloadSender implements Sender<Payload> {

    @Override
    public void send(AsynchronousSocketChannel channel, Payload payload) {
        if (payload instanceof TextPayload textPayload) {
            new TextSender().send(channel, textPayload.getText());
        } else if (payload instanceof FilePayload filePayload) {
            new FileSender().send(channel, filePayload.getPath());
        }
    }

    @Override
    public void send(SocketChannel channel, Payload payload) {
        if (payload instanceof TextPayload textPayload) {
            new TextSender().send(channel, textPayload.getText());
        } else if (payload instanceof FilePayload filePayload) {
            new FileSender().send(channel, filePayload.getPath());
        }
    }
}
