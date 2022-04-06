package com.sushi.components.senders;

import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.PayloadContext;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;

public class PayloadSender {

    public void send(AsynchronousByteChannel channel, PayloadContext payloadContext) {
        ContentType contentType = payloadContext.payloadMetaData().contentType();
        (ContentType.FILE.equals(contentType) ? new FileSender() : new TextSender()).send(channel,
            payloadContext.payload().content(), null);
    }

    public void send(ByteChannel channel, PayloadContext payloadContext) {
        ContentType contentType = payloadContext.payloadMetaData().contentType();
        (ContentType.FILE.equals(contentType) ? new FileSender() : new TextSender()).send(channel,
            payloadContext.payload().content());
    }

}
