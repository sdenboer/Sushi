package com.sushi.server.pull;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.protocol.pull.PullOrderMapper;
import com.sushi.components.sender.asynchronous.ServingSender;
import com.sushi.components.utils.OrderContext;
import com.sushi.components.utils.Utils;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.sushi.components.utils.Constants.FILE_DIR;

public class PullOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PullOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message,
                       OrderContext orderContext) {
        PullOrder order = new PullOrderMapper().from(message);
        try {
            Path path = Paths.get(FILE_DIR, order.getDir(), order.getFileName());
            long size = Files.size(path);
            logger.info(LoggerUtils.createMessage(orderContext) + "pulling " + path + " of "
                    + Utils.bytesToFileSize(size));
            ServingSender.sendFilePayload(socketChannel, path, size, orderContext);

        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            ServingSender.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
        }
    }

}
