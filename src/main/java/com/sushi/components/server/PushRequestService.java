package com.sushi.components.server;

import com.sushi.components.file_transfer.receive.FileWriter;
import com.sushi.components.protocol.Response;
import com.sushi.components.protocol.order.push.SushiPushOrder;
import com.sushi.components.utils.Constants;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class PushRequestService {

  private final SushiPushOrder order;

  public PushRequestService(SushiPushOrder order) {
    this.order = order;
  }

  public void handle(AsynchronousSocketChannel channel) {
    try {
      FileWriter fileWriterProxy = new FileWriter(order.getDir(), order.getFileName(), order.getFileSize());
      read(channel, fileWriterProxy);
    } catch (IOException e) {
      close(channel, null);
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
            sendResponse(channel, new Response(200));
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

  private void sendResponse(final AsynchronousSocketChannel channel, Response response) {

    final ByteBuffer buffer = ByteBuffer.wrap(response.toString().getBytes(StandardCharsets.UTF_8));
    channel.write(buffer, null, new CompletionHandler<Integer, Void>() {

      @Override
      public void completed(final Integer result, final Void attachment) {
        while (buffer.hasRemaining()) {
          channel.write(buffer, attachment, this);
        }
        try {
          channel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void failed(final Throwable exc, final Void attachment) {
        close(channel, null);
        throw new RuntimeException("unable to confirm", exc);
      }
    });
  }
}
