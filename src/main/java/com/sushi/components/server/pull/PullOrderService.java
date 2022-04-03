package com.sushi.components.server.pull;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.NotFoundException;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.protocol.pull.SushiPullOrder;
import com.sushi.components.common.protocol.pull.SushiPullServing;
import com.sushi.components.common.senders.SushiMessageSender;
import com.sushi.components.server.OrderService;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PullOrderService implements OrderService<SushiPullOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, SushiPullOrder sushiOrder, OrderContext orderContext) {
        UUID orderId = sushiOrder.getOrderId();
        try {
            Path path = Paths.get(sushiOrder.getDir(), sushiOrder.getFileName());
            long size = Files.size(path);
            FilePayload payload = new FilePayload(path);
            SushiPullServing serving = new SushiPullServing(SushiServingStatus.OK, orderId, "aes", "file", size, payload);
            new SushiMessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            throw new NotFoundException(e, orderId);
        }
    }

}
