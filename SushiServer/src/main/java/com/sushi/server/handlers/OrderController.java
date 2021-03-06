package com.sushi.server.handlers;

import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.utils.OrderContext;
import com.sushi.server.file.FileOrderService;
import com.sushi.server.pull.PullOrderService;
import com.sushi.server.push.PushOrderService;
import com.sushi.server.remove.RemoveOrderService;
import com.sushi.server.status.StatusOrderService;

import java.nio.channels.AsynchronousByteChannel;

public class OrderController {

    private final AsynchronousByteChannel channel;

    public OrderController(AsynchronousByteChannel channel) {
        this.channel = channel;
    }

    public void handleOrder(OrderMethod method, String message, OrderContext orderContext) {

        (switch (method) {
            case PUSH -> new PushOrderService();
            case PULL -> new PullOrderService();
            case FILE -> new FileOrderService();
            case REMOVE -> new RemoveOrderService();
            case STATUS -> new StatusOrderService();
        }).handle(channel, message, orderContext);
    }


}
