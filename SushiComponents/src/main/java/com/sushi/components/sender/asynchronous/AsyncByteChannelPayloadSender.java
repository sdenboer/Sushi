package com.sushi.components.sender.asynchronous;

import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.PayloadContext;

import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Path;

public class AsyncByteChannelPayloadSender {

    public void send(AsynchronousByteChannel channel, PayloadContext payloadContext) {
        ContentType contentType = payloadContext.payloadMetaData().contentType();
        if (ContentType.TXT.equals(contentType)) {
            new AsyncByteChannelTextSender().send(channel, payloadContext.payload().content());
        } else {
            new AsyncByteChannelFileSender().send(channel, Path.of(payloadContext.payload().content()));
        }
    }

}
