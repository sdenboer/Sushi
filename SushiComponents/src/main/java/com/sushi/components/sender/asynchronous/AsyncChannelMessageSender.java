package com.sushi.components.sender.asynchronous;

import com.sushi.components.message.Message;
import com.sushi.components.sender.SenderStrategy;
import com.sushi.components.utils.OnComplete;
import org.apache.log4j.Logger;

import java.nio.channels.AsynchronousByteChannel;

public class AsyncChannelMessageSender implements SenderStrategy<AsynchronousByteChannel, Message> {

    private static final Logger logger = Logger.getLogger(AsyncChannelMessageSender.class);

    @Override
    public void send(AsynchronousByteChannel socketChannel, Message message) {
        OnComplete sendPayloadOnComplete = null;
        logger.info("sending message: " + message.toRequest());
        if (message.hasPayload()) {
            sendPayloadOnComplete = () -> new AsyncByteChannelPayloadSender().send(socketChannel,
                    message.getPayloadContext());
        }
        new AsyncByteChannelTextSender(sendPayloadOnComplete).send(socketChannel, message.toRequest());
    }
}
