package com.sushi.client.push;

import com.sushi.client.order.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingMapper;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.sender.synchronous.ByteChannelMessageSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

import static com.sushi.components.message.serving.ServingStatus.OK;

public class PushOrderService implements OrderService {

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {
        new ByteChannelMessageSender().send(socketChannel, order);
        String response = receiveServing(socketChannel);
        Serving serving = new ServingMapper().from(response);
        if (serving.getServingStatus().equals(OK)) {
            String payload = receiveTextPayload(socketChannel, serving.getPayloadContext());
            System.out.println(payload);
        }
        if (ServingStatus.PERMISSION_DENIED.equals(serving.getServingStatus())) {
            System.out.println("File is being written by another process. Please try again later");
        }
        return serving;
    }

}
