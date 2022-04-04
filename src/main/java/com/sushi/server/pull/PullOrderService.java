package com.sushi.server.pull;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.NotFoundException;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.ContentType;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.protocol.pull.PullOrder;
import com.sushi.components.common.protocol.pull.PullOrderMapper;
import com.sushi.components.common.protocol.pull.PullServing;
import com.sushi.components.common.senders.MessageSender;
import com.sushi.server.OrderService;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PullOrderService implements OrderService {

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext) {
        PullOrder order = new PullOrderMapper().from(message);
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
