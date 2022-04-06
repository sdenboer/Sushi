package com.sushi.server.remove;

import static com.sushi.components.utils.Constants.FILE_DIR;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.remove.RemoveOrder;
import com.sushi.components.protocol.remove.RemoveOrderMapper;
import com.sushi.components.senders.ServingSender;
import com.sushi.components.utils.OrderContext;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.log4j.Logger;

public class RemoveOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(RemoveOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message,
        OrderContext orderContext) {
        RemoveOrder order = new RemoveOrderMapper().from(message);
        Path path = Paths.get(FILE_DIR, order.getDir(), order.getFileName());
        try {
            Files.delete(path);
            ServingSender.send(socketChannel, orderContext);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            ServingSender.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
        }

    }


}
