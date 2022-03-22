package com.sushi.components.protocol.order.push;

import com.sushi.components.protocol.order.SushiOrderService;
import com.sushi.components.utils.Constants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SushiPushOrderService implements SushiOrderService<SushiPushOrder> {

  @Override
  public void send(SushiPushOrder sushiOrder) {
    InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
    try (SocketChannel socketChannel = SocketChannel.open(hostAddress); FileChannel fileChannel = FileChannel.open(Paths.get(sushiOrder.getSrcPath()),
        StandardOpenOption.READ)) {

      write(socketChannel, sushiOrder);
      transferFile(fileChannel, socketChannel, sushiOrder);
      readResponse(fileChannel, socketChannel);
    } catch (IOException ioe) {
      System.out.println("TODO");
    }
  }


  private void transferFile(FileChannel fileChannel, SocketChannel socketChannel, SushiPushOrder sushiOrder) throws IOException {
    long position = 0L;
    long size = fileChannel.size();
    while (position < size) {
      position += fileChannel.transferTo(position, Constants.TRANSFER_MAX_SIZE, socketChannel);
    }
  }

  private void readResponse(FileChannel fileChannel, SocketChannel socketChannel) throws IOException {
    StringBuilder response = new StringBuilder();
    while (!response.toString().contains("response")) {
      final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
      final long bytesRead = socketChannel.read(buffer);
      if (bytesRead > 0) {
        response.append(new String(buffer.array()));
      } else if (bytesRead < 0) {
        socketChannel.close();
        fileChannel.close();
      }
    }
    System.out.println(response);
  }

}
