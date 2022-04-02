package com.sushi.components.server;

import com.sushi.components.common.file.SushiFileServing;
import com.sushi.components.common.file.SushiFileServingPayload;
import com.sushi.components.common.file_transfer.FileSender;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.pull.SushiPullServingPayload;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingPayload;
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

    public ServingService(AsynchronousSocketChannel socketChannel, SushiServing serving) {
        this.socketChannel = socketChannel;
        this.serving = serving;
    }


    public void send() {
        final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {

            @Override
            public void completed(final Integer result, final Void attachment) {
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer, attachment, this);
                }
                if (Objects.nonNull(serving.getPayload())) {
                    if (serving instanceof SushiPullServing) {
                        SushiPullServingPayload payload = (SushiPullServingPayload) serving.getPayload();
                        FileSender.transferFile(socketChannel,  payload.getPath());
                    } else if (serving instanceof SushiFileServing) {
                        SushiFileServingPayload payload = (SushiFileServingPayload) serving.getPayload();
                        final ByteBuffer payloadBuffer = ByteBuffer.wrap(payload.toRequest().getBytes(StandardCharsets.UTF_8));
                        socketChannel.write(payloadBuffer, null, new CompletionHandler<Integer, Void>() {
                            @Override
                            public void completed(Integer result, Void attachment) {
                                while (payloadBuffer.hasRemaining()) {
                                    socketChannel.write(payloadBuffer, attachment, this);
                                }
                            }

                            @Override
                            public void failed(Throwable exc, Void attachment) {
                                exc.printStackTrace();
                            }
                        });
                    }
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
