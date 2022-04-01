package com.sushi.components.server;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.pull.SushiPullOrder;

import java.nio.channels.AsynchronousSocketChannel;

public interface OrderService<T extends SushiOrder> {

    void handle(AsynchronousSocketChannel socketChannel, T sushiOrder, OrderContext orderContext);

}
