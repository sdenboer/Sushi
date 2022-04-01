package com.sushi.components.server;

import com.sushi.components.common.file.FileSender;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.utils.ChannelUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public class ServingService {

    private final AsynchronousSocketChannel socketChannel;
    private final SushiServing serving;
    private Path path;

    public ServingService(AsynchronousSocketChannel socketChannel, SushiServing serving) {
        this.socketChannel = socketChannel;
        this.serving = serving;
    }

    public void addFile(Path path) {
        this.path = path;
    }

    public void send() {
        final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {

            @Override
            public void completed(final Integer result, final Void attachment) {
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer, attachment, this);
                }
                if (Objects.nonNull(path)) {
                    FileSender.transferFile(socketChannel, path);
                } else {
                    ChannelUtils.close(socketChannel);
                }
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
                ChannelUtils.close(socketChannel);
                throw new RuntimeException("unable to confirm", exc);
            }
        });
    }

}
