package com.sushi.server.status;

import static com.sushi.components.utils.Constants.FILE_DIR;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.senders.ServingSender;
import com.sushi.components.utils.OrderContext;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

public class StatusOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(StatusOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message,
        OrderContext orderContext) {
        Path path = Paths.get(FILE_DIR);
        try (SequenceInputStream stream = new SequenceInputStream(
            getInputStreamsOfFilesInDirectory(path).elements())) {
            String payloadMessage = DigestUtils.sha256Hex(stream);
            ServingSender.sendTextPayload(socketChannel, payloadMessage, orderContext);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            ServingSender.send(socketChannel, ServingStatus.SERVER_ERROR, orderContext);
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
