package com.sushi.components.sender.synchronous;

import com.sushi.components.message.Message;
import com.sushi.components.sender.SenderStrategy;

import java.nio.channels.ByteChannel;

public class ByteChannelMessageSender implements SenderStrategy<ByteChannel, Message> {

    @Override
    public void send(ByteChannel socketChannel, Message content) {
        new ByteChannelTextSender().send(socketChannel, content.toRequest());
        if (content.hasPayload()) {
            new ByteChannelPayloadSender().send(socketChannel, content.getPayloadContext());
        }
    }
}
