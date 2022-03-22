package com.sushi.components.file_transfer.send;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class FileReader {

  private FileChannel channel;
  private FileSender sender;

  public FileReader(final FileSender sender, final String path) throws IOException {
    if (Objects.isNull(sender) || StringUtils.isEmpty(path)) {
      throw new IllegalArgumentException("sender and path required");
    }

    this.sender = sender;
    FileInputStream inputStream  =  new FileInputStream(Paths.get(path).toFile());
    FileOutputStream fileOutputStream = copy(inputStream);
    this.channel = fileOutputStream.getChannel();
//    this.channel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
  }
  FileOutputStream copy(InputStream source) throws IOException {
    FileOutputStream target = new FileOutputStream("test");
    byte[] buf = new byte[8192];
    int length;
    while ((length = source.read(buf)) > 0) {
      target.write(buf, 0, length);
    }
    return target;
  }

  public void read() throws IOException {
    try {
      transfer();
    } finally {
      close();
    }
  }

  public void close() throws IOException {
    this.sender.close();
    this.channel.close();
  }

  private void transfer() throws IOException {
    this.sender.transfer(this.channel, 0L, this.channel.size());
  }
}