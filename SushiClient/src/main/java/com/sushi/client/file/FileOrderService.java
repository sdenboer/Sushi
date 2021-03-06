package com.sushi.client.file;

import com.sushi.client.order.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingMapper;
import com.sushi.components.sender.synchronous.ByteChannelTextSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

import static com.sushi.components.message.serving.ServingStatus.OK;

public class FileOrderService implements OrderService {

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {
        new ByteChannelTextSender().send(socketChannel, order.toRequest());
        String serving = receiveServing(socketChannel);
        Serving fileServing = new ServingMapper().from(serving);
        if (fileServing.getServingStatus().equals(OK)) {
            String response = receiveTextPayload(socketChannel, fileServing.getPayloadContext());
            System.out.println(response);
        }
        return fileServing;

    }
}
