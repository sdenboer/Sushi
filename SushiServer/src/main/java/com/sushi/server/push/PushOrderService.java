package com.sushi.server.push;

import com.sushi.components.FileWriter;
import com.sushi.components.OrderContext;
import com.sushi.components.error.exceptions.AbortedException;
import com.sushi.components.error.exceptions.InvalidRequestException;
import com.sushi.components.error.exceptions.ServerErrorException;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.protocol.push.PushOrder;
import com.sushi.components.protocol.push.PushOrderMapper;
import com.sushi.components.protocol.push.PushServing;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;
import com.sushi.server.OrderService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class PushOrderService implements OrderService {

    @Override
    public void handle(AsynchronousByteChannel socketChannel, String message, OrderContext orderContext) {
        PushOrder order = new PushOrderMapper().from(message);
        try {
            Files.createDirectories(Path.of(order.getDir()));
            FileWriter fileWriter = new FileWriter(order.getDir(), order.getFileName(), order.getFileSize());
            read(socketChannel, fileWriter, orderContext);
        } catch (IOException e) {
            throw new ServerErrorException(e, orderContext.getOrderId());
        }
    }

    private void read(final AsynchronousByteChannel socketChannel, final FileWriter fileWriter, OrderContext orderContext) {

        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        socketChannel.read(buffer, fileWriter, new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final FileWriter attachedFileWriter) {

                if (result >= 0) {
                    if (result > 0) {
                        writeToFile(buffer, attachedFileWriter);
                    }
                    if (attachedFileWriter.done()) {
                        PushServing serving = new PushServing(ServingStatus.OK, UUID.randomUUID(), "txt");
                        new MessageSender().send(socketChannel, serving);
                        ChannelUtils.close(attachedFileWriter.getFileChannel());
                    } else {
                        buffer.clear();
                        socketChannel.read(buffer, attachedFileWriter, this);
                    }
                } else {
                    ChannelUtils.close(fileWriter.getFileChannel());
                    throw new InvalidRequestException(orderContext.getOrderId());
                }
            }


            @Override
            public void failed(final Throwable exc, final FileWriter failedFileWriter) {

                throw new RuntimeException("unable to read data", exc);
            }

            private void writeToFile(final ByteBuffer buffer, final FileWriter fileWriter) {

                try {
                    buffer.flip();

                    final long bytesWritten = fileWriter.write(buffer, fileWriter.getPosition().get());
                    fileWriter.getPosition().addAndGet(bytesWritten);
                } catch (IOException e) {
                    e.printStackTrace();
                    ChannelUtils.close(fileWriter.getFileChannel());
                    throw new AbortedException(orderContext.getOrderId());
                }
            }
        });
    }
}
