package com.sushi.components.server.remove;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.NotFoundException;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.protocol.remove.RemoveOrder;
import com.sushi.components.common.protocol.remove.RemoveServing;
import com.sushi.components.common.senders.MessageSender;
import com.sushi.components.server.OrderService;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoveOrderService implements OrderService<RemoveOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, RemoveOrder order, OrderContext orderContext) {
        Path path = Paths.get(order.getDir(), order.getFileName());
        try {
            Files.delete(path);
            RemoveServing serving = new RemoveServing(ServingStatus.OK, order.getOrderId());
            new MessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            throw new NotFoundException(e, orderContext.getOrderId());
        }

    }


}
