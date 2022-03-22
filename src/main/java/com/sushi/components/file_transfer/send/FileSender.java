package com.sushi.components.file_transfer.send;

import com.sushi.components.utils.Constants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import javax.crypto.CipherInputStream;

public final class FileSender {

  private final InetSocketAddress hostAddress;
  private final SocketChannel socketChannel;

  public FileSender(String host, final int port) throws IOException {
    this.hostAddress = new InetSocketAddress(host, port);
    this.socketChannel = SocketChannel.open(this.hostAddress);
  }

  public void transfer(final FileChannel fileChannel, long position, long size) throws IOException {


    while (position < size) {
      position += fileChannel.transferTo(position, Constants.TRANSFER_MAX_SIZE, this.socketChannel);
    }
  }

  public void transferTest() throws IOException {
    ByteBuffer buffer = ByteBuffer.wrap("Hello".getBytes());
    this.socketChannel.write(buffer);
  }

  public SocketChannel getChannel() {
    return this.socketChannel;
  }

  public void close() throws IOException {
    this.socketChannel.close();
  }
}