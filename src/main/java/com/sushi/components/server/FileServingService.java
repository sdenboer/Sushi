package com.sushi.components.server;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class FileServingService {

  private final AsynchronousSocketChannel socketChannel;

  public FileServingService(AsynchronousSocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  @SneakyThrows
  public void sendServing(SushiServing serving) {

    final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));

    FileChannel fileChannel = FileChannel.open(Paths.get("/tmp/input/test.txt"), StandardOpenOption.READ);
    final long[] l = {0, 0};

    socketChannel.write(buffer, fileChannel, new CompletionHandler<>() {

      @Override
      public void completed(Integer result, FileChannel attachment) {
        final ByteBuffer fileBuffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        System.out.println("Sending 1: " + result);
        try {
          if (result > 0) {
            int read = attachment.read(fileBuffer);
            l[0] += read;
            l[1] += result;
            fileBuffer.flip();
            socketChannel.write(fileBuffer, attachment, this);
          } else {
            System.out.println("DONE +" + attachment.size() + " " + l[0] + " " + l[1] + " " + result);
            fileChannel.close();
            socketChannel.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void failed(Throwable exc, FileChannel attachment) {

      }

    });
  }
}
