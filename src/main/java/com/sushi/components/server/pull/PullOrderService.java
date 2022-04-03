package com.sushi.components.server.pull;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.NotFoundException;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.ContentType;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.protocol.pull.PullOrder;
import com.sushi.components.common.protocol.pull.PullServing;
import com.sushi.components.common.senders.MessageSender;
import com.sushi.components.server.OrderService;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PullOrderService implements OrderService<PullOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, PullOrder order, OrderContext orderContext) {
        UUID orderId = order.getOrderId();
        try {
            Path path = Paths.get(order.getDir(), order.getFileName());
            long size = Files.size(path);
            FilePayload payload = new FilePayload(path);
            PullServing serving = new PullServing(ServingStatus.OK, orderId, "aes", ContentType.FILE, size, payload);
            new MessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            throw new NotFoundException(e, orderId);
        }
    }

}
