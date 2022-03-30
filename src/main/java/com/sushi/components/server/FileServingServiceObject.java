package com.sushi.components.server;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

public class FileServingServiceObject {

  private final AtomicLong position;
  private final FileChannel fileChannel;

  public FileServingServiceObject() throws IOException {
    this.fileChannel = FileChannel.open(Paths.get("/tmp/input/test.txt"), StandardOpenOption.READ);
    this.position = new AtomicLong(0L);
  }

  public AtomicLong getPosition() {
    return position;
  }

  public void close() throws IOException {
    fileChannel.close();
  }


}
