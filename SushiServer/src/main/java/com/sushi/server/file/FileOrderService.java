package com.sushi.server.file;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.TextPayload;
import com.sushi.components.protocol.file.FileOrder;
import com.sushi.components.protocol.file.FileOrderMapper;
import com.sushi.components.protocol.file.FileServing;
import com.sushi.components.senders.MessageSender;
import com.sushi.server.OrderContext;
import com.sushi.server.OrderService;
import com.sushi.server.utils.LoggerUtils;
import com.sushi.server.exceptions.SushiError;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sushi.components.utils.Constants.FILE_DIR;

public class FileOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(FileOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext) {
        FileOrder order = new FileOrderMapper().from(message);
        Map<String, String> files = new HashMap<>();
        Path path = Paths.get(FILE_DIR, order.getDir(), order.getFileName());
        try {
            if (Files.isDirectory(path)) {
                for (Path file : getFilesInDirectory(path)) {
                    files.put(file.toString(), getSHA265HexFromPath(file));
                }
            } else {
                files.put(path.toString(), getSHA265HexFromPath(path));
            }
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
            e.printStackTrace();
        }

        String payload = serializePayload(files);
        int payloadSize = payload.getBytes(StandardCharsets.UTF_8).length;
        TextPayload textPayload = new TextPayload(payload);
        FileServing serving = new FileServing(ServingStatus.OK, order.getOrderId(), ContentType.TXT, payloadSize, textPayload);
        new MessageSender().send(socketChannel, serving);
    }

    private String serializePayload(Map<String, String> files) {
        return files.entrySet()
                .stream()
                .map(s -> s.getKey() + ": " + s.getValue())
                .collect(Collectors.joining("\n"));
    }

    private String getSHA265HexFromPath(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            return DigestUtils.sha256Hex(is);
        }
    }

    private List<Path> getFilesInDirectory(Path dir) throws IOException {
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths.map(Path::normalize)
                    .filter(Files::isRegularFile).toList();
        }
    }

}
