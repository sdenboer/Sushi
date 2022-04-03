package com.sushi.components.server.file;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.NotFoundException;
import com.sushi.components.common.error.exceptions.ServerErrorException;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.TextPayload;
import com.sushi.components.common.protocol.file.FileOrder;
import com.sushi.components.common.protocol.file.FileServing;
import com.sushi.components.common.senders.MessageSender;
import com.sushi.components.server.OrderService;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOrderService implements OrderService<FileOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, FileOrder order, OrderContext orderContext) {

        Map<String, String> files = new HashMap<>();
        if (order.getFileName() == null) {
            Path dir = Paths.get(order.getDir());
            getFilesInDirectory(dir, orderContext).forEach(file -> files.put(file.toString(), getSHA265HexFromPath(file, orderContext)));
        } else {
            Path file = Paths.get(order.getDir(), order.getFileName());
            files.put(file.toString(), getSHA265HexFromPath(file, orderContext));
        }

        String payload = serializePayload(files);
        int payloadSize = payload.getBytes(StandardCharsets.UTF_8).length;
        TextPayload textPayload = new TextPayload(payload);
        FileServing serving = new FileServing(ServingStatus.OK, order.getOrderId(), "txt", payloadSize, textPayload);
        new MessageSender().send(socketChannel, serving);
    }

    private String serializePayload(Map<String, String> files) {
        return files.entrySet()
                .stream()
                .map(s -> s.getKey() + ": " + s.getValue())
                .collect(Collectors.joining("\n"));
    }

    private String getSHA265HexFromPath(Path path, OrderContext orderContext) {
        try (InputStream is = Files.newInputStream(path)) {
            return DigestUtils.sha256Hex(is);
        } catch (IOException e) {
            throw new ServerErrorException(e, orderContext.getOrderId());
        }
    }

    private List<Path> getFilesInDirectory(Path dir, OrderContext orderContext) {
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths.map(Path::normalize)
                    .filter(Files::isRegularFile).toList();
        } catch (IOException e) {
            throw new NotFoundException(e, orderContext.getOrderId());
        }
    }

}
