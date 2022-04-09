package com.sushi.components.sender.asynchronous;

import com.sushi.components.sender.SenderStrategy;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class AsyncByteChannelFileSender implements SenderStrategy<AsynchronousByteChannel, Path> {

    private static final Logger logger = Logger.getLogger(AsyncByteChannelFileSender.class);

    @Override
    public void send(AsynchronousByteChannel socketChannel, Path path) {

        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);

        try {
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            fileChannel.read(buffer);
            buffer.flip();

            socketChannel.write(buffer, fileChannel, new CompletionHandler<>() {
                @Override
                public void completed(Integer result, FileChannel completionFileChannel) {
                    if (result <= 0) {
                        ChannelUtils.close(completionFileChannel, socketChannel);
                    } else {
                        buffer.compact();
                        try {
                            completionFileChannel.read(buffer);
                            buffer.flip();
                            socketChannel.write(buffer, completionFileChannel, this);
                        } catch (IOException e) {
                            logger.error(e);
                            ChannelUtils.close(completionFileChannel, socketChannel);
                        }
                    }
                }

                @Override
                public void failed(Throwable e, FileChannel attachment) {
                    logger.error(e);
                    ChannelUtils.close(attachment, socketChannel);
                }
            });
        } catch (IOException e) {
            logger.error(e);
            ChannelUtils.close(socketChannel);
        }

    }

}
