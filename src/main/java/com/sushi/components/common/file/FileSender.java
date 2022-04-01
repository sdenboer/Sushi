package com.sushi.components.common.file;

import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileSender {

    public static void transferFile(SocketChannel socketChannel, String path) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ)) {
            long position = 0L;
            long size = fileChannel.size();
            while (position < size) {
                position += fileChannel.transferTo(position, Constants.TRANSFER_MAX_SIZE, socketChannel);
            }
        }
    }

    public static void transferFile(AsynchronousSocketChannel socketChannel, Path path) {

        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        try {
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            fileChannel.read(buffer);
            buffer.flip();

            socketChannel.write(buffer, fileChannel, new CompletionHandler<>() {
                @Override
                public void completed(Integer result, FileChannel attachment) {
                    if (result <= 0) {
                        ChannelUtils.close(socketChannel, fileChannel);
                    } else {
                        buffer.clear();
                        try {
                            attachment.read(buffer);
                            buffer.flip();
                            socketChannel.write(buffer, attachment, this);
                        } catch (IOException e) {
                            ChannelUtils.close(socketChannel, fileChannel);
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, FileChannel attachment) {
                    ChannelUtils.close(socketChannel);
                }
            });
        } catch (IOException e) {
            ChannelUtils.close(socketChannel);
        }


    }


}
