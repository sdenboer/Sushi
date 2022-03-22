package com.sushi.components.file_transfer.send;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

public abstract class Sender<T extends Channel> {

  protected SocketChannel client;

  public Sender(String host, final int port) throws IOException {
    InetSocketAddress hostAddress = new InetSocketAddress(host, port);
    this.client = SocketChannel.open(hostAddress);
  }


  public abstract void transfer(T channel, long position, long size) throws IOException;
}
