package com.sushi.server;

import java.nio.channels.AsynchronousByteChannel;

public interface OrderService {

    void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext);

}
