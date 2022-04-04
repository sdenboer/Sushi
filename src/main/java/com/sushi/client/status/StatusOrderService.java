package com.sushi.client.status;

import com.sushi.client.OrderService;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.protocol.status.StatusServing;
import com.sushi.components.common.protocol.status.StatusServingMapper;
import com.sushi.components.common.senders.TextSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

import static com.sushi.components.common.message.serving.ServingStatus.OK;

public class StatusOrderService implements OrderService {

    @Override
    public StatusServing send(ByteChannel socketChannel, Order order) throws IOException {

        new TextSender().send(socketChannel, order.toRequest());
        String serving = receiveServing(socketChannel);
        StatusServing statusServing = new StatusServingMapper().from(serving);
        if (statusServing.getServingStatus().equals(OK)) {
            String response = receiveTextPayload(socketChannel, statusServing.getPayloadSize());
            System.out.println(response);
        }
        return statusServing;
    }
}
