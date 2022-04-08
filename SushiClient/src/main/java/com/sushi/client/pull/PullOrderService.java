package com.sushi.client.pull;

import static com.sushi.components.message.serving.ServingStatus.OK;
import static com.sushi.components.utils.Constants.FILE_DIR;

import com.sushi.client.order.OrderService;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingMapper;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.FileWriter;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import org.apache.log4j.Logger;

public class PullOrderService implements OrderService {

    private static final Logger logger = Logger.getLogger(PullOrderService.class);

    @Override
    public Serving send(ByteChannel socketChannel, Order order) throws IOException {
        MessageSender.send(socketChannel, order);
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
            FileWriter fileWriter = new FileWriter(FILE_DIR, order.getFileName(),
                pullServing.getPayloadContext().payloadMetaData().contentLength());
            while(!fileWriter.done()) {
                fileWriter.write(socketChannel);
            }
            fileWriter.finish();
        } catch (IOException e) {
            System.out.println("Problem receiving file");
            logger.error(e);
        }
    }

}
