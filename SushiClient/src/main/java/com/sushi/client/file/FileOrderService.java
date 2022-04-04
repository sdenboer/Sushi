package com.sushi.client.file;

import com.sushi.client.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.protocol.file.FileServing;
import com.sushi.components.protocol.file.FileServingMapper;
import com.sushi.components.senders.TextSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

import static com.sushi.components.message.serving.ServingStatus.OK;

public class FileOrderService implements OrderService {

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {
        new TextSender().send(socketChannel, order.toRequest());
        String serving = receiveServing(socketChannel);
        FileServing fileServing = new FileServingMapper().from(serving);
        if (fileServing.getServingStatus().equals(OK)) {
            String response = receiveTextPayload(socketChannel, fileServing.getPayloadSize());
            System.out.println(response);
        }
        return fileServing;

    }
}
