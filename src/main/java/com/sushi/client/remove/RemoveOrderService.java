package com.sushi.client.remove;

import com.sushi.client.OrderService;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.protocol.remove.RemoveServing;
import com.sushi.components.common.protocol.remove.RemoveServingMapper;
import com.sushi.components.common.senders.TextSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

public class RemoveOrderService implements OrderService {

    @Override
    public RemoveServing send(ByteChannel socketChannel, Order order) throws IOException {
        new TextSender().send(socketChannel, order.toRequest());
        String serving = receiveServing(socketChannel);
        return new RemoveServingMapper().from(serving);
    }
}
