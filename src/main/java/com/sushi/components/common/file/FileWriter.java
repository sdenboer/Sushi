package com.sushi.components.common.file;

import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

public class FileWriter {

    private final FileChannel fileChannel;
    private final AtomicLong position;
    private final long fileSize;

    public FileWriter(String dir, String fileName, long fileSize) throws IOException {
        Path path = Paths.get(dir, fileName);
        this.fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        this.position = new AtomicLong(0L);
        this.fileSize = fileSize;
    }

    public int write(ByteBuffer buffer, long position) throws IOException {
        int bytesWritten = 0;
        while (buffer.hasRemaining()) {
            bytesWritten += this.fileChannel.write(buffer, position + bytesWritten);
        }

        return bytesWritten;
    }

    public void write(SocketChannel socketChannel) throws IOException {
        while (!done()) {
            position.addAndGet(fileChannel.transferFrom(socketChannel, position.get(), Constants.TRANSFER_MAX_SIZE));

        }
    }

    public AtomicLong getPosition() {
        return position;
    }

    public boolean done() {
        double f = ((double) this.position.get() / this.fileSize * 100);


        BigDecimal bd = new BigDecimal(f).setScale(2, RoundingMode.HALF_UP);
        System.out.println(bd);
        return this.position.get() == this.fileSize;
    }

    public FileChannel getFileChannel() {
        return fileChannel;
    }
}
