package com.sushi.server.handlers;

import com.sushi.components.utils.OrderContext;
import java.nio.channels.AsynchronousByteChannel;

public interface OrderService {

    void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext);

}
