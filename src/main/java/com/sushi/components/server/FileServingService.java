package com.sushi.components.server;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import lombok.SneakyThrows;

public class FileServingService {

  private final AsynchronousSocketChannel socketChannel;

  public FileServingService(AsynchronousSocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  @SneakyThrows
  public void sendServing(SushiServing serving, Path path) {

    final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));
    socketChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {
      @Override
      public void completed(Integer result, Void attachment) {
        if (result >= 0) {
          if (result > 0) {
            try {
              writeFile(serving, path);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        } else {
          ChannelUtils.close(socketChannel);
        }
      }

      @Override
      public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace();
      }
    });
  }

  private void writeFile(SushiServing serving, Path path) throws IOException {

    final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
    FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
    fileChannel.read(buffer);
    buffer.flip();

    socketChannel.write(buffer, fileChannel, new CompletionHandler<>() {
      @Override
      public void completed(Integer result, FileChannel attachment) {
        if (result >= 0) {
          if (result > 0) {
            buffer.clear();
            try {
              attachment.read(buffer);
              buffer.flip();
              socketChannel.write(buffer, attachment, this);
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else {
            try {
              attachment.close();
              ChannelUtils.close(socketChannel);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }

        } else {
          ChannelUtils.close(socketChannel);
        }
      }

      @Override
      public void failed(Throwable exc, FileChannel attachment) {

      }
    });
  }

//    socketChannel.write(buffer, fileChannel, new CompletionHandler<>() {
//
//      @Override
//      public void completed(Integer result, FileChannel attachment) {
//
//        try {
//          if (result > 0) {
////            int read = attachment.read(buffer);
////            l[0] += read;
////            l[1] += result;
////            buffer.flip();
////            socketChannel.write(buffer, attachment, this);
//          } else {
//            System.out.println("DONE +" + attachment.size() + " " + l[0] + " " + l[1] + " " + result);
//            fileChannel.close();
//            socketChannel.close();
//          }
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//
//      @Override
//      public void failed(Throwable exc, FileChannel attachment) {
//        exc.printStackTrace();
//      }

//    });

}
