package com.sushi.server.remove;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.remove.RemoveOrder;
import com.sushi.components.protocol.remove.RemoveOrderMapper;
import com.sushi.components.protocol.remove.RemoveServing;
import com.sushi.components.senders.MessageSender;
import com.sushi.server.utils.OrderContext;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import com.sushi.server.exceptions.SushiError;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.sushi.components.utils.Constants.FILE_DIR;

public class RemoveOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(RemoveOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext) {
        RemoveOrder order = new RemoveOrderMapper().from(message);
        Path path = Paths.get(FILE_DIR, order.getDir(), order.getFileName());
        try {
            Files.delete(path);
            RemoveServing serving = new RemoveServing(ServingStatus.OK, order.getOrderId());
            new MessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
        }

    }


}
