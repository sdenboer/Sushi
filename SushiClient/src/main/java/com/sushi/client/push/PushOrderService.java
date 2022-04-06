package com.sushi.client.push;

import com.sushi.client.order.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.push.PushServing;
import com.sushi.components.protocol.push.PushServingMapper;
import com.sushi.components.senders.MessageSender;
import java.io.IOException;
import java.nio.channels.ByteChannel;

public class PushOrderService implements OrderService {

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {
        new MessageSender().send(socketChannel, order);
        String response = receiveServing(socketChannel);
        PushServing serving = new PushServingMapper().from(response);
        if (ServingStatus.PERMISSION_DENIED.equals(serving.getServingStatus())) {
            System.out.println("File is being written by another process. Please try again later");
        }
        return serving;
    }

}
