package com.sushi.components.common.file;

import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileSender {

    public static void transferFile(SocketChannel socketChannel, String srcPath) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(Paths.get(srcPath), StandardOpenOption.READ)) {
            long position = 0L;
            long size = fileChannel.size();
            while (position < size) {
                position += fileChannel.transferTo(position, Constants.TRANSFER_MAX_SIZE, socketChannel);
            }
        }
    }



}
