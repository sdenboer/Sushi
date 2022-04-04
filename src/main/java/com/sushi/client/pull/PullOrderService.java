package com.sushi.client.pull;

import com.sushi.client.OrderService;
import com.sushi.components.common.FileWriter;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.protocol.pull.PullOrder;
import com.sushi.components.common.protocol.pull.PullServing;
import com.sushi.components.common.protocol.pull.PullServingMapper;
import com.sushi.components.common.senders.MessageSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

import static com.sushi.components.common.message.serving.ServingStatus.OK;
import static com.sushi.components.utils.Constants.TMP_DIR;

public class PullOrderService implements OrderService {

    @Override
    public Serving handle(ByteChannel socketChannel, Order order) throws IOException {
        new MessageSender().send(socketChannel, order);
        String serving = receiveServing(socketChannel);
        PullServing pullServing = new PullServingMapper().from(serving);
        if (pullServing.getServingStatus().equals(OK)) {
            receiveFilePayload(socketChannel, pullServing, (PullOrder) order);
        }
        return pullServing;
    }

    private void receiveFilePayload(ByteChannel socketChannel, PullServing pullServing, PullOrder order) {

        try {
            FileWriter fileWriter = new FileWriter(TMP_DIR, order.getFileName(), pullServing.getFileSize());
            fileWriter.write(socketChannel);
            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == pullServing.getFileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
