package com.sushi.server.file;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.file.FileOrder;
import com.sushi.components.protocol.file.FileOrderMapper;
import com.sushi.components.sender.asynchronous.ServingSender;
import com.sushi.components.utils.OrderContext;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.sushi.components.utils.Constants.FILE_DIR;
import static com.sushi.server.utils.FileUtils.*;

public class FileOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(FileOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message,
                       OrderContext orderContext) {
        FileOrder order = new FileOrderMapper().from(message);
        Map<String, String> files = new HashMap<>();
        String[] paths = Stream.of(order.getDir(), order.getFileName())
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        Path path = Paths.get(FILE_DIR, paths);
        logger.info(LoggerUtils.createMessage(orderContext) + "listing files in " + path);
        try {
            addPathChecksumToFiles(path, files);
            String payloadText = filesToPayload(files);
            ServingSender.sendTextPayload(socketChannel, payloadText, orderContext);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            ServingSender.send(socketChannel, ServingStatus.NOT_FOUND, orderContext);
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
