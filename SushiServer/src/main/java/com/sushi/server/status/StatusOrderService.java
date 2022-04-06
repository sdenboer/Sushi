package com.sushi.server.status;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.TextPayload;
import com.sushi.components.protocol.status.StatusOrder;
import com.sushi.components.protocol.status.StatusOrderMapper;
import com.sushi.components.protocol.status.StatusServing;
import com.sushi.components.senders.MessageSender;
import com.sushi.server.utils.OrderContext;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.exceptions.SushiError;
import com.sushi.server.utils.LoggerUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import static com.sushi.components.utils.Constants.FILE_DIR;

public class StatusOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(StatusOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext) {
        StatusOrder order = new StatusOrderMapper().from(message);
        Path path = Paths.get(FILE_DIR);
        try (SequenceInputStream stream = new SequenceInputStream(getInputStreamsOfFilesInDirectory(path).elements())) {
            String payload = DigestUtils.sha256Hex(stream);
            int payloadSize = payload.getBytes(StandardCharsets.UTF_8).length;
            TextPayload textPayload = new TextPayload(payload);
            StatusServing serving = new StatusServing(ServingStatus.OK, order.getOrderId(), ContentType.TXT, payloadSize, textPayload);
            new MessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.SERVER_ERROR, orderContext);
        }

    }

    private Vector<InputStream> getInputStreamsOfFilesInDirectory(Path dir) throws IOException {
        Vector<InputStream> vector = new Vector<>();
        try (Stream<Path> paths = Files.walk(dir)) {
            List<Path> pathList = paths.map(Path::normalize).filter(Files::isRegularFile).toList();
            for (Path path : pathList) {
                vector.addElement(Files.newInputStream(path));
            }
        }
        return vector;
    }
}
