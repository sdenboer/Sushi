package com.sushi.components.server.remove;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.NotFoundException;
import com.sushi.components.common.message.order.SushiRemoveOrder;
import com.sushi.components.common.message.serving.SushiRemoveServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.senders.SushiMessageSender;
import com.sushi.components.server.OrderService;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoveOrderService implements OrderService<SushiRemoveOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, SushiRemoveOrder sushiOrder, OrderContext orderContext) {
        Path path = Paths.get(sushiOrder.getDir(), sushiOrder.getFileName());
        try {
            Files.delete(path);
            SushiRemoveServing serving = new SushiRemoveServing(SushiServingStatus.OK, sushiOrder.getOrderId());
            new SushiMessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            throw new NotFoundException(e, orderContext.getOrderId());
        }

    }


}
