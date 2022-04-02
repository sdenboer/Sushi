package com.sushi.components.server;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.message.order.SushiOrder;

import java.nio.channels.AsynchronousSocketChannel;

public interface OrderService<T extends SushiOrder> {

    void handle(AsynchronousSocketChannel socketChannel, T sushiOrder, OrderContext orderContext);

}
