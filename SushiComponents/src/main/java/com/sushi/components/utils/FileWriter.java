package com.sushi.components.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Getter;

@Getter
public class FileWriter {

    private final FileChannel fileChannel;
    private final AtomicLong position;
    private final long fileSize;
    private final Path path;

    public FileWriter(String dir, String fileName, long fileSize) throws IOException {
        this.path = Paths.get(dir, fileName);
        this.fileChannel = FileChannel.open(path, StandardOpenOption.WRITE,
            StandardOpenOption.CREATE);
        this.position = new AtomicLong(0L);
        this.fileSize = fileSize;
        //lock for other processes
        fileChannel.tryLock();
    }

    public int write(ByteBuffer buffer, long position) throws IOException {
        int bytesWritten = 0;
        while (buffer.hasRemaining()) {
            bytesWritten += this.fileChannel.write(buffer, position + bytesWritten);
        }

        return bytesWritten;
    }

    public void write(ByteChannel socketChannel) throws IOException {
        while (!done()) {
            position.addAndGet(fileChannel.transferFrom(socketChannel, position.get(),
                Constants.TRANSFER_MAX_SIZE));
        }
        ChannelUtils.close(fileChannel);
    }

    public boolean done() {
        return this.position.get() == this.fileSize;
    }

}
