package com.sushi.components.server.push;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.InvalidRequestException;
import com.sushi.components.common.error.exceptions.ServerErrorException;
import com.sushi.components.common.file_transfer.FileWriter;
import com.sushi.components.common.message.order.SushiPushOrder;
import com.sushi.components.common.message.serving.SushiPushServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.senders.SushiMessageSender;
import com.sushi.components.server.OrderService;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.UUID;

public class PushOrderService implements OrderService<SushiPushOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, SushiPushOrder order, OrderContext orderContext) {

        try {
            FileWriter fileWriter = new FileWriter(order.getDir(), order.getFileName(), order.getFileSize());
            read(socketChannel, fileWriter, orderContext);
        } catch (IOException e) {
            throw new ServerErrorException(e, orderContext.getOrderId());
        }
    }

    private void read(final AsynchronousSocketChannel socketChannel, final FileWriter fileWriter, OrderContext orderContext) {

        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        socketChannel.read(buffer, fileWriter, new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final FileWriter attachedFileWriter) {

                if (result >= 0) {
                    if (result > 0) {
                        writeToFile(socketChannel, buffer, attachedFileWriter);
                    }
                    if (attachedFileWriter.done()) {
                        SushiPushServing serving = new SushiPushServing(SushiServingStatus.OK, UUID.randomUUID(), "txt");
                        new SushiMessageSender().send(socketChannel, serving);
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

            private void writeToFile(final AsynchronousSocketChannel socketChannel, final ByteBuffer buffer, final FileWriter fileWriter) {

                try {
                    buffer.flip();

                    final long bytesWritten = fileWriter.write(buffer, fileWriter.getPosition().get());
                    fileWriter.getPosition().addAndGet(bytesWritten);
                } catch (IOException e) {
                    ChannelUtils.close(socketChannel, fileWriter.getFileChannel());
                }
            }
        });
    }
}
