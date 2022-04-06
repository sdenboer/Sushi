package com.sushi.server.push;

import static com.sushi.components.utils.Constants.FILE_DIR;
import static com.sushi.server.utils.FileUtils.filesToPayload;
import static com.sushi.server.utils.FileUtils.getSHA265HexFromPath;

import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.push.PushOrder;
import com.sushi.components.protocol.push.PushOrderMapper;
import com.sushi.components.senders.ServingSender;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;
import com.sushi.components.utils.FileWriter;
import com.sushi.components.utils.OrderContext;
import com.sushi.components.utils.Utils;
import com.sushi.server.exceptions.SushiError;
import com.sushi.server.handlers.OrderService;
import com.sushi.server.utils.LoggerUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.log4j.Logger;

public class PushOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PushOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message,
        OrderContext orderContext) {
        PushOrder order = new PushOrderMapper().from(message);
        try {
            Files.createDirectories(Paths.get(FILE_DIR, order.getDir()));
            FileWriter fileWriter = new FileWriter(FILE_DIR + order.getDir(), order.getFileName(),
                order.getPayloadContext().payloadMetaData().contentLength());
            read(socketChannel, fileWriter, orderContext);
        } catch (OverlappingFileLockException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.PERMISSION_DENIED, orderContext);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.SERVER_ERROR, orderContext);
        }
    }

    private void read(final AsynchronousByteChannel socketChannel, final FileWriter fileWriter,
        OrderContext orderContext) {
        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        logger.info(LoggerUtils.createMessage(orderContext) + "waiting for file...");
        socketChannel.read(buffer, fileWriter, new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final FileWriter attachedFileWriter) {
                if (result >= 0) {
                    if (result > 0) {
                        try {
                            writeToFile(buffer, attachedFileWriter);
                        } catch (IOException e) {
                            ChannelUtils.close(fileWriter.getFileChannel());
                            logger.error(LoggerUtils.createMessage(orderContext), e);
                            SushiError.send(socketChannel, ServingStatus.ABORTED, orderContext);
                        }
                    }
                    if (attachedFileWriter.done()) {
                        sendSuccessResponse(attachedFileWriter, socketChannel, orderContext);
                    } else {
                        buffer.clear();
                        socketChannel.read(buffer, attachedFileWriter, this);
                    }
                } else {
                    ChannelUtils.close(fileWriter.getFileChannel());
                    logger.info(LoggerUtils.createMessage(orderContext) + "Problem with buffer");
                    SushiError.send(socketChannel, ServingStatus.INVALID, orderContext);
                }
            }

            @Override
            public void failed(final Throwable exc, final FileWriter failedFileWriter) {
                logger.error(LoggerUtils.createMessage(orderContext), exc);
                SushiError.send(socketChannel, ServingStatus.INVALID, orderContext);
            }

            private void writeToFile(final ByteBuffer buffer, final FileWriter fileWriter)
                throws IOException {
                buffer.flip();
                final long bytesWritten = fileWriter.write(buffer, fileWriter.getPosition().get());
                fileWriter.getPosition().addAndGet(bytesWritten);
            }
        });
    }

    private void sendSuccessResponse(FileWriter fileWriter, AsynchronousByteChannel socketChannel,
        OrderContext orderContext) {
        logger.info(LoggerUtils.createMessage(orderContext)
            + "finished writing "
            + Utils.bytesToFileSize(fileWriter.getFileSize())
            + " to file " + fileWriter.getPath());
        try {
            String hash = getSHA265HexFromPath(fileWriter.getPath());
            String payloadMessage = filesToPayload(Map.of(fileWriter.getPath().toString(), hash));
            ServingSender.send(socketChannel, payloadMessage, orderContext);
        } catch (IOException e) {
            logger.info(LoggerUtils.createMessage(orderContext) + "Problem with buffer");
            SushiError.send(socketChannel, ServingStatus.INVALID, orderContext);
        } finally {
            ChannelUtils.close(fileWriter.getFileChannel());
        }


    }
}
