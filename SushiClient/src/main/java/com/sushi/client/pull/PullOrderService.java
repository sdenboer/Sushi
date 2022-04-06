package com.sushi.client.pull;

import static com.sushi.client.utils.Constants.TMP_DIR;
import static com.sushi.components.message.serving.ServingStatus.OK;

import com.sushi.client.order.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.protocol.pull.PullServing;
import com.sushi.components.protocol.pull.PullServingMapper;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.FileWriter;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import org.apache.log4j.Logger;

public class PullOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PullOrderService.class);

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {
        new MessageSender().send(socketChannel, order);
        String serving = receiveServing(socketChannel);
        PullServing pullServing = new PullServingMapper().from(serving);
        if (pullServing.getServingStatus().equals(OK)) {
            receiveFilePayload(socketChannel, pullServing, (PullOrder) order);
        }
        return pullServing;
    }

    private void receiveFilePayload(ByteChannel socketChannel, PullServing pullServing,
        PullOrder order) {
        try {
            FileWriter fileWriter = new FileWriter(TMP_DIR, order.getFileName(),
                pullServing.getFileSize());
            fileWriter.write(socketChannel);
        } catch (IOException e) {
            System.out.println("Problem receiving file");
            logger.error(e);
        }
    }

}
