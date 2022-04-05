package com.sushi.server.pull;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.FilePayload;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.protocol.pull.PullOrderMapper;
import com.sushi.components.protocol.pull.PullServing;
import com.sushi.components.senders.MessageSender;
import com.sushi.server.OrderContext;
import com.sushi.server.OrderService;
import com.sushi.server.utils.LoggerUtils;
import com.sushi.server.exceptions.SushiError;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sushi.components.utils.Constants.FILE_DIR;

public class PullOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PullOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext) {
        PullOrder order = new PullOrderMapper().from(message);
        UUID orderId = order.getOrderId();
        try {
            Path path = Paths.get(FILE_DIR, order.getDir(), order.getFileName());
            long size = Files.size(path);
            logger.info(LoggerUtils.createMessage(orderContext) + "pulling " + path + " of " + (size / (1024 * 1024)) + " MB");
            FilePayload payload = new FilePayload(path);
            PullServing serving = new PullServing(ServingStatus.OK, orderId, "aes", ContentType.FILE, size, payload);
            new MessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
            e.printStackTrace();
        }
    }

}
