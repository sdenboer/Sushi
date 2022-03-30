package com.sushi.components.common.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

public class FileWriter {

    private final FileChannel fileChannel;
    private final AtomicLong position;
    private final long fileSize;

    public FileWriter(String dir, String fileName, long fileSize) throws IOException {
        String path = dir + "/" + fileName;
        this.fileChannel = FileChannel.open(Paths.get(path), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        this.position = new AtomicLong(0L);
        this.fileSize = fileSize;
    }

    public int write(final ByteBuffer buffer, long position) throws IOException {
        int bytesWritten = 0;
        while (buffer.hasRemaining()) {
            bytesWritten += this.fileChannel.write(buffer, position + bytesWritten);
        }

        return bytesWritten;
    }

    public AtomicLong getPosition() {
        return position;
    }

    public boolean done() {
        return this.position.get() == this.fileSize;
    }

    public void close() throws IOException {
        this.fileChannel.close();
    }

    public FileChannel getFileChannel() {
        return fileChannel;
    }
}
