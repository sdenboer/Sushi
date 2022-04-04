package com.sushi.server;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.message.order.Order;

import java.nio.channels.AsynchronousByteChannel;

public interface OrderService<T extends Order> {

    void handle(AsynchronousByteChannel socketChannel, T order, OrderContext orderContext);

}
