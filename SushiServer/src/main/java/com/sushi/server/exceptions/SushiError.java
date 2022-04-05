package com.sushi.server.exceptions;

import com.sushi.components.error.ErrorServing;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.senders.MessageSender;
import com.sushi.server.OrderContext;

import java.nio.channels.AsynchronousByteChannel;

public class SushiError {

    public static void send(AsynchronousByteChannel channel, ServingStatus status, OrderContext orderContext) {
        sendMessage(channel, status, orderContext);
    }

    private static void sendMessage(AsynchronousByteChannel channel, ServingStatus status, OrderContext orderContext) {
        ErrorServing serving = new ErrorServing(status, orderContext.orderId());
        new MessageSender().send(channel, serving);
    }

}
