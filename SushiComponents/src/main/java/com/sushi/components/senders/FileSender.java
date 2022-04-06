package com.sushi.components.senders;

import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.Constants;
import com.sushi.components.utils.OnComplete;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.log4j.Logger;

public class FileSender implements Sender {

    private static final Logger logger = Logger.getLogger(FileSender.class);

    @Override
    public void send(ByteChannel socketChannel, String path) {
        try (FileChannel fileChannel = FileChannel.open(Path.of(path), StandardOpenOption.READ)) {
            long position = 0L;
            long size = fileChannel.size();

            while (position < size) {
                position += fileChannel.transferTo(position, Constants.TRANSFER_MAX_SIZE,
                    socketChannel);
            }
        } catch (IOException e) {
            System.out.println("Problem sending file");
            logger.error(e);
        }
    }

    @Override
    public void send(AsynchronousByteChannel socketChannel, String path, OnComplete onComplete) {

        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);

        try {
            FileChannel fileChannel = FileChannel.open(Path.of(path), StandardOpenOption.READ);
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
