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
import java.time.Duration;
import java.time.Instant;

import com.sushi.components.utils.Utils;
import org.apache.log4j.Logger;

import static java.time.Instant.now;

public class FileSender implements Sender {

    private static final Logger logger = Logger.getLogger(FileSender.class);

    @Override
    public void send(ByteChannel socketChannel, String path) {
        try (FileChannel fileChannel = FileChannel.open(Path.of(path), StandardOpenOption.READ)) {

            int percentageOfTransfer;
            long dataTransferredPerStream = 0;
            final long size = fileChannel.size();

            Instant totalTime = now();
            Instant timePassed = now();

            System.out.println("Sending " + Utils.bytesToFileSize(size) + " file...");
            long position = 0L;
            while (position < size) {
                long dataTransferred = fileChannel.transferTo(position, Constants.TRANSFER_MAX_SIZE, socketChannel);
                position += dataTransferred;
                dataTransferredPerStream += dataTransferred;
                Instant timeAfterTransfer = now();
                long seconds = Duration.between(timePassed, timeAfterTransfer).getSeconds();
                if (seconds > 0) {
                    timePassed = timeAfterTransfer;
                    percentageOfTransfer = getPercentage(position, size);
                    long dataTransferredPerSecond = dataTransferredPerStream / seconds;
                    dataTransferredPerStream = 0;
                    System.out.print("\r");
                    System.out.printf("%s%% - %s/s", percentageOfTransfer, Utils.bytesToFileSize(dataTransferredPerSecond));
                }
            }
            System.out.printf("\rFinished transferring in %s second(s)%n", Duration.between(totalTime, now()).toSeconds());
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

    private int getPercentage(long current, long total) {
        return (int) (current * 100.0 / total + 0.5);
    }
}
