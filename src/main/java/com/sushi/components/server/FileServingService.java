package com.sushi.components.server;

import com.sushi.components.common.file.FileSender;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileServingService {

    private final AsynchronousSocketChannel socketChannel;

    public FileServingService(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @SneakyThrows
    public void sendServing(SushiServing serving, Path path) {

        final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer, null, this);
                }
                try {
                    FileSender.transferFile(socketChannel, path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });
    }

}
