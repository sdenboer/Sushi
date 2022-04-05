package com.sushi.server.push;

import com.sushi.components.FileWriter;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.push.PushOrder;
import com.sushi.components.protocol.push.PushOrderMapper;
import com.sushi.components.protocol.push.PushServing;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;
import com.sushi.server.OrderContext;
import com.sushi.server.OrderService;
import com.sushi.server.utils.LoggerUtils;
import com.sushi.server.exceptions.SushiError;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sushi.components.utils.Constants.FILE_DIR;

public class PushOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PushOrderService.class);

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext) {
        PushOrder order = new PushOrderMapper().from(message);
        try {
            Files.createDirectories(Paths.get(FILE_DIR, order.getDir()));
            FileWriter fileWriter = new FileWriter(FILE_DIR + order.getDir(), order.getFileName(), order.getFileSize());
            read(socketChannel, fileWriter, orderContext);
        } catch (IOException e) {
            logger.error(LoggerUtils.createMessage(orderContext), e);
            SushiError.send(socketChannel, ServingStatus.SERVER_ERROR, orderContext);
            e.printStackTrace();
        }
    }

    private void read(final AsynchronousByteChannel socketChannel, final FileWriter fileWriter, OrderContext orderContext) {
        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        logger.info(LoggerUtils.createMessage(orderContext) + "attempting to write file");
        socketChannel.read(buffer, fileWriter, new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final FileWriter attachedFileWriter) {
                if (result >= 0) {
                    if (result > 0) {
                        writeToFile(buffer, attachedFileWriter);
                    }
                    if (attachedFileWriter.done()) {
                        logger.info(LoggerUtils.createMessage(orderContext)
                                + "finished writing "
                                + (attachedFileWriter.getPosition().get() / (1024 * 1024))
                                + " MB to file " + attachedFileWriter.getPath());
                        PushServing serving = new PushServing(ServingStatus.OK, UUID.randomUUID(), "txt");
                        new MessageSender().send(socketChannel, serving);
                        ChannelUtils.close(attachedFileWriter.getFileChannel());
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

            private void writeToFile(final ByteBuffer buffer, final FileWriter fileWriter) {

                try {
                    buffer.flip();

                    final long bytesWritten = fileWriter.write(buffer, fileWriter.getPosition().get());
                    fileWriter.getPosition().addAndGet(bytesWritten);
                } catch (IOException e) {
                    ChannelUtils.close(fileWriter.getFileChannel());
                    logger.error(LoggerUtils.createMessage(orderContext), e);
                    SushiError.send(socketChannel, ServingStatus.ABORTED, orderContext);
                    e.printStackTrace();
                }
            }
        });
    }
}
