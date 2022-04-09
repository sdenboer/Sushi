package com.sushi.client.status;

import com.sushi.client.order.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingMapper;
import com.sushi.components.sender.synchronous.ByteChannelTextSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

import static com.sushi.components.message.serving.ServingStatus.OK;

public class StatusOrderService implements OrderService {

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {

        new ByteChannelTextSender().send(socketChannel, order.toRequest());
        String message = receiveServing(socketChannel);
        Serving serving = new ServingMapper().from(message);
        if (serving.getServingStatus().equals(OK)) {
            String response = receiveTextPayload(socketChannel, serving.getPayloadContext());
            System.out.println(response);
        }
        return serving;
    }
}
