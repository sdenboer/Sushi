package com.sushi.server;

import com.sushi.components.OrderContext;

import java.nio.channels.AsynchronousByteChannel;

public interface OrderService {

    void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext);

}
