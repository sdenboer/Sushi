package com.sushi.components.server.pull;

import com.sushi.components.common.file.FileSender;
import com.sushi.components.common.file.FileWriter;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.push.SushiPushServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.server.FileServingService;
import com.sushi.components.server.OrderService;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;

public class PullOrderService extends OrderService<SushiPullOrder> {

    public PullOrderService(AsynchronousSocketChannel channel) {
        super(channel);
    }

    @Override
    public void handle(SushiPullOrder sushiOrder) {
        read(channel, sushiOrder);
    }

    private void read(final AsynchronousSocketChannel channel, SushiPullOrder sushiPullOrder) {

        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        buffer.flip();

        channel.read(buffer, sushiPullOrder, new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final SushiPullOrder completedFileWriter) {

                if (result >= 0) {
                    SushiPullServing serving = new SushiPullServing(SushiServingStatus.OK);
                    new FileServingService(channel).sendServing(serving);

//                    if (completedFileWriter.done()) {
//                        SushiPushServing txt = new SushiPushServing(SushiServingStatus.OK, "txt");
//                        sendTextResponse(txt);
//                    } else {
//                        buffer.clear();
//                        channel.read(buffer, completedFileWriter, this);
//                    }
                } else {
                    channel.read(buffer, completedFileWriter, this);
                }
            }


            @Override
            public void failed(final Throwable exc, final SushiPullOrder failedFileWriter) {
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
}
