package com.sushi.components.senders;

import com.sushi.components.utils.OnComplete;
import com.sushi.components.message.wrappers.FilePayload;
import com.sushi.components.message.wrappers.Payload;
import com.sushi.components.message.wrappers.TextPayload;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;

public class PayloadSender implements Sender<Payload> {

    @Override
    public void send(AsynchronousByteChannel channel, Payload payload, OnComplete onComplete) {
        if (payload instanceof TextPayload textPayload) {
            new TextSender().send(channel, textPayload.getText(), null);
        } else if (payload instanceof FilePayload filePayload) {
            new FileSender().send(channel, filePayload.getPath(), null);
        }
    }

    @Override
    public void send(ByteChannel channel, Payload payload) throws IOException {
        if (payload instanceof TextPayload textPayload) {
            new TextSender().send(channel, textPayload.getText());
        } else if (payload instanceof FilePayload filePayload) {
            new FileSender().send(channel, filePayload.getPath());
        }
    }
}
