package com.sushi.components.server.pull;

import com.sushi.components.common.file.FileWriter;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.server.FileServingService;
import com.sushi.components.server.OrderService;
import com.sushi.components.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
      public void completed(final Integer result, final SushiPullOrder sushiPullOrder) {

        if (result >= 0) {

          try {
            Path path = Paths.get(sushiPullOrder.getDir(), sushiPullOrder.getFileName());
            long size = Files.size(path);
            SushiPullServing serving = new SushiPullServing(SushiServingStatus.OK, sushiPullOrder.getOrderId(), "aes", "file", size);
            new FileServingService(channel).sendServing(serving, path);
          } catch (IOException e) {
            e.printStackTrace();
          }

        } else {
          channel.read(buffer, sushiPullOrder, this);
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
