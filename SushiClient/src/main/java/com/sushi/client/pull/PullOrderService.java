package com.sushi.client.pull;

import com.sushi.client.order.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingMapper;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.sender.synchronous.ByteChannelMessageSender;
import com.sushi.components.utils.FileWriter;
import com.sushi.components.utils.Utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.time.Duration;
import java.time.Instant;

import static com.sushi.components.message.serving.ServingStatus.OK;
import static java.time.Instant.now;

public class PullOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PullOrderService.class);

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {
        new ByteChannelMessageSender().send(socketChannel, order);
        String message = receiveServing(socketChannel);
        Serving serving = new ServingMapper().from(message);
        if (serving.getServingStatus().equals(OK)) {
            receiveFilePayload(socketChannel, serving, (PullOrder) order);
        }
        return serving;
    }

    private void receiveFilePayload(ByteChannel socketChannel, Serving pullServing,
                                    PullOrder order) {
        try {
            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir"), order.getFileName(),
                    pullServing.getPayloadContext().payloadMetaData().contentLength());

            long dataTransferredPerStream = 0;
            final long size = fileWriter.getFileSize();

            Instant totalTime = now();
            Instant timePassed = now();

            System.out.println("Receiving " + Utils.bytesToFileSize(fileWriter.getFileSize()) + " file...");
            while (!fileWriter.done()) {
                long dataTransferred = fileWriter.write(socketChannel);
                dataTransferredPerStream += dataTransferred;
                Instant timeAfterTransfer = now();
                long seconds = Duration.between(timePassed, timeAfterTransfer).getSeconds();
                if (seconds > 0) {
                    System.out.print("\r");
                    System.out.printf("%s%% - %s/s", Utils.getPercentage(fileWriter.getPosition().get(), size),
                            Utils.bytesToFileSize(dataTransferredPerStream / seconds));
                    timePassed = timeAfterTransfer;
                    dataTransferredPerStream = 0;
                }
            }
            System.out.printf("\rFinished transferring in %s second(s)%n", Duration.between(totalTime, now()).toSeconds());
            fileWriter.finish();
        } catch (IOException e) {
            System.out.println("Problem receiving file");
            logger.error(e);
        }
    }

}
