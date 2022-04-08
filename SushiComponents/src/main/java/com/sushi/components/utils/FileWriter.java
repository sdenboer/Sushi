package com.sushi.components.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import org.apache.log4j.Logger;

@Getter
public class FileWriter {

    private static final Logger logger = Logger.getLogger(FileWriter.class);

    private final FileChannel fileChannel;
    private final AtomicLong position;
    private final long fileSize;
    private final Path path;
    private final Path tmpPath;

    public FileWriter(String dir, String fileName, long fileSize) throws IOException {
        this.path = Paths.get(dir, fileName);
        this.tmpPath = Paths.get(dir, fileName + Constants.TMP_FILE_SUFFIX);
        this.fileChannel = FileChannel.open(tmpPath, StandardOpenOption.WRITE,
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
        position.addAndGet(fileChannel.transferFrom(socketChannel, position.get(),
                Constants.TRANSFER_MAX_SIZE));
    }

    public boolean done() {
        return this.position.get() == this.fileSize;
    }

    public void finish() {
        ChannelUtils.close(fileChannel);
        try {
            Files.move(tmpPath, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void fail() {
        ChannelUtils.close(fileChannel);
        try {
            Files.delete(tmpPath);
        } catch (IOException e) {
            logger.error(e);
        }
    }

}
