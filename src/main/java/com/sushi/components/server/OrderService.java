package com.sushi.components.server;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.message.order.Order;

import java.nio.channels.AsynchronousSocketChannel;

public interface OrderService<T extends Order> {

    void handle(AsynchronousSocketChannel socketChannel, T order, OrderContext orderContext);

}
