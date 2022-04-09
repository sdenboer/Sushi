package com.sushi.components.sender.synchronous;

import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.sender.SenderStrategy;

import java.nio.channels.ByteChannel;
import java.nio.file.Path;

public class ByteChannelPayloadSender implements SenderStrategy<ByteChannel, PayloadContext> {
    @Override
    public void send(ByteChannel socketChannel, PayloadContext content) {
        ContentType contentType = content.payloadMetaData().contentType();
        System.out.println(contentType);
        if (ContentType.TXT.equals(contentType)) {
            new ByteChannelTextSender().send(socketChannel, content.payload().content());
        } else {
            new ByteChannelFileSender().send(socketChannel, Path.of(content.payload().content()));
        }
    }
}
