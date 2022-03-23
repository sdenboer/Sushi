package com.sushi.components.server;

import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.serving.SushiServing;

import java.nio.channels.AsynchronousSocketChannel;

public abstract class OrderService<T extends SushiOrder> {

    protected final TextServingResponse textServingResponse;
    protected final AsynchronousSocketChannel channel;

    public OrderService(AsynchronousSocketChannel channel) {
        this.channel = channel;
        this.textServingResponse = new TextServingResponse(channel);
    }

    public abstract void handle(T sushiOrder);

    protected void sendTextResponse(SushiServing serving) {
        textServingResponse.sendServing(serving);
    }
}
