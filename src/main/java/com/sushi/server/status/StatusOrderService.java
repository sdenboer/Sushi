package com.sushi.server.status;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.NotFoundException;
import com.sushi.components.common.error.exceptions.ServerErrorException;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.ContentType;
import com.sushi.components.common.message.wrappers.TextPayload;
import com.sushi.components.common.protocol.status.StatusOrder;
import com.sushi.components.common.protocol.status.StatusServing;
import com.sushi.components.common.senders.MessageSender;
import com.sushi.server.OrderService;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sushi.components.utils.Constants.TMP_DIR;

public class StatusOrderService implements OrderService<StatusOrder> {

    @Override
    public void handle(AsynchronousByteChannel socketChannel, StatusOrder order, OrderContext orderContext) {

        Path path = Paths.get(TMP_DIR);
        Vector<InputStream> inputStreams = getInputStreamsOfFilesInDirectory(path, orderContext);
        try (SequenceInputStream stream = new SequenceInputStream(inputStreams.elements())) {
            String payload = DigestUtils.sha256Hex(stream);
            int payloadSize = payload.getBytes(StandardCharsets.UTF_8).length;
            TextPayload textPayload = new TextPayload(payload);
            StatusServing serving = new StatusServing(ServingStatus.OK, order.getOrderId(), ContentType.TXT, payloadSize, textPayload);
            new MessageSender().send(socketChannel, serving);
        } catch (IOException e) {
            throw new ServerErrorException(e, order.getOrderId());
        }

    }

    private Vector<InputStream> getInputStreamsOfFilesInDirectory(Path dir, OrderContext orderContext) {
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.newInputStream(path);
                        } catch (IOException e) {
                            throw new ServerErrorException(e, orderContext.getOrderId());
                        }
                    }).collect(Collectors.toCollection(Vector::new));
        } catch (IOException e) {
            throw new NotFoundException(e, orderContext.getOrderId());
        }
    }
}
