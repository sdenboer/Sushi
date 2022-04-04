package com.sushi.server;

import com.sushi.components.common.OrderContext;

import java.nio.channels.AsynchronousByteChannel;

public interface OrderService {

    void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext);

}
