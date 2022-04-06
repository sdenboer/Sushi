package com.sushi.server.exceptions;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.OrderContext;
import java.nio.channels.AsynchronousByteChannel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SushiError {

    public static void send(AsynchronousByteChannel channel, ServingStatus status,
        OrderContext orderContext) {
        sendMessage(channel, status, orderContext);
    }

    private static void sendMessage(AsynchronousByteChannel channel, ServingStatus status,
        OrderContext orderContext) {
        Serving serving = new Serving(status, orderContext.orderId(), null);
        MessageSender.send(channel, serving);
    }

}
