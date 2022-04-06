package com.sushi.server.file;

import static com.sushi.components.utils.Constants.FILE_DIR;
import static com.sushi.server.utils.FileUtils.filesToPayload;
import static com.sushi.server.utils.FileUtils.getSHA265HexFromPath;
import static com.sushi.server.utils.FileUtils.removeBaseDir;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.Payload;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.protocol.file.FileOrder;
import com.sushi.components.protocol.file.FileOrderMapper;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.OrderContext;
import com.sushi.server.exceptions.SushiError;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

public class FileOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(FileOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message,
        OrderContext orderContext) {
        FileOrder order = new FileOrderMapper().from(message);
        Map<String, String> files = new HashMap<>();
        Path path = Paths.get(FILE_DIR, order.getDir(), order.getFileName());
        try {
            addPathChecksumToFiles(path, files);
            String payloadText = filesToPayload(files);
            int payloadSize = payloadText.getBytes(StandardCharsets.UTF_8).length;
            Payload payload = new Payload(payloadText);
            PayloadMetaData metaData = new PayloadMetaData(ContentType.TXT, payloadSize);
            Serving serving = new Serving(ServingStatus.OK, order.getOrderId(),
                new PayloadContext(metaData, payload));
            MessageSender.send(socketChannel, serving);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
        }

    }

    private void addPathChecksumToFiles(Path path, Map<String, String> files) throws IOException {
        if (Files.isDirectory(path)) {
            for (Path file : getFilesInDirectory(path)) {
                addFileChecksumToFiles(file, files);
            }
        } else {
            addFileChecksumToFiles(path, files);
        }
    }

    private void addFileChecksumToFiles(Path file, Map<String, String> files) throws IOException {
        String cleanPath = removeBaseDir(file);
        files.put(cleanPath, getSHA265HexFromPath(file));
    }


    private List<Path> getFilesInDirectory(Path dir) throws IOException {
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths.map(Path::normalize)
                .filter(Files::isRegularFile).toList();
        }
    }

}
