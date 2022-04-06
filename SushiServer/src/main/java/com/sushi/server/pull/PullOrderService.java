package com.sushi.server.pull;

import static com.sushi.components.utils.Constants.FILE_DIR;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.Payload;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.protocol.pull.PullOrderMapper;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.OrderContext;
import com.sushi.components.utils.Utils;
import com.sushi.server.exceptions.SushiError;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.log4j.Logger;

public class PullOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PullOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message,
        OrderContext orderContext) {
        PullOrder order = new PullOrderMapper().from(message);
        UUID orderId = order.getOrderId();
        try {
            Path path = Paths.get(FILE_DIR, order.getDir(), order.getFileName());
            long size = Files.size(path);
            logger.info(LoggerUtils.createMessage(orderContext) + "pulling " + path + " of "
                + Utils.bytesToFileSize(size));
            Payload payload = new Payload(path.toString());
            PayloadMetaData payloadMetaData = new PayloadMetaData(ContentType.FILE, size);
            Serving serving = new Serving(ServingStatus.OK, orderId,
                new PayloadContext(payloadMetaData, payload));
            MessageSender.send(socketChannel, serving);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
        }
    }

}
