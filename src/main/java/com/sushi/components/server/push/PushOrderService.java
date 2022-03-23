package com.sushi.components.server.push;

import com.sushi.components.common.file.FileWriter;
import com.sushi.components.common.push.SushiPushOrder;
import com.sushi.components.common.push.SushiPushServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.server.OrderService;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class PushOrderService extends OrderService<SushiPushOrder> {

    public PushOrderService(AsynchronousSocketChannel channel) {
        super(channel);
    }

    @Override
    public void handle(SushiPushOrder order) {
        try {
            FileWriter fileWriter = new FileWriter(order.getDir(), order.getFileName(), order.getFileSize());
            read(channel, fileWriter);
        } catch (IOException e) {
            ChannelUtils.close(channel);
            throw new RuntimeException("unable to create fileWriter", e);
        }
    }

    private void close(final AsynchronousSocketChannel channel, final FileWriter fileWriter) {
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException("unable to close channel and FileWriter", e);
        }
    }

    private void read(final AsynchronousSocketChannel channel, final FileWriter fileWriter) {

        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        channel.read(buffer, fileWriter, new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final FileWriter completedFileWriter) {

                if (result >= 0) {
                    if (result > 0) {
                        writeToFile(channel, buffer, completedFileWriter);
                    }
                    if (completedFileWriter.done()) {
                        SushiPushServing txt = new SushiPushServing(SushiServingStatus.OK, "txt");
                        sendTextResponse(txt);
                    } else {
                        buffer.clear();
                        channel.read(buffer, completedFileWriter, this);
                    }
                } else {
                    close(channel, fileWriter);
                }
            }


            @Override
            public void failed(final Throwable exc, final FileWriter failedFileWriter) {
                throw new RuntimeException("unable to read data", exc);
            }

            private void writeToFile(final AsynchronousSocketChannel channel, final ByteBuffer buffer, final FileWriter fileWriter) {

                try {
                    buffer.flip();

                    final long bytesWritten = fileWriter.write(buffer, fileWriter.getPosition().get());
                    fileWriter.getPosition().addAndGet(bytesWritten);
                } catch (IOException e) {
                    close(channel, fileWriter);
                    throw new RuntimeException("unable to write bytes to file", e);
                }
            }
        });
    }
}
