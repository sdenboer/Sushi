package com.sushi.components.sender.synchronous;

import com.sushi.components.sender.SenderStrategy;
import com.sushi.components.utils.Constants;
import com.sushi.components.utils.Utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

import static java.time.Instant.now;

public class ByteChannelFileSender implements SenderStrategy<ByteChannel, Path> {

    private static final Logger logger = Logger.getLogger(ByteChannelFileSender.class);

    @Override
    public void send(ByteChannel socketChannel, Path path) {
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {

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
                    System.out.print("\r");
                    System.out.printf("%s%% - %s/s", Utils.getPercentage(position, size),
                            Utils.bytesToFileSize(dataTransferredPerStream / seconds));
                    timePassed = timeAfterTransfer;
                    dataTransferredPerStream = 0;
                }
            }
            System.out.printf("\rFinished transferring in %s second(s)%n", Duration.between(totalTime, now()).toSeconds());
        } catch (IOException e) {
            System.out.println("Problem sending file");
            logger.error(e);
        }
    }
}
