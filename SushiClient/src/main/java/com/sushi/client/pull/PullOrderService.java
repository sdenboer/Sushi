package com.sushi.client.pull;

import com.sushi.client.OrderService;
import com.sushi.components.FileWriter;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.protocol.pull.PullServing;
import com.sushi.components.protocol.pull.PullServingMapper;
import com.sushi.components.senders.MessageSender;

import java.io.IOException;
import java.nio.channels.ByteChannel;

import static com.sushi.client.Constants.WORKING_DIR_PROPERTY;
import static com.sushi.components.message.serving.ServingStatus.OK;

public class PullOrderService implements OrderService {

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

    private void receiveFilePayload(ByteChannel socketChannel, PullServing pullServing, PullOrder order) {
        final String dir = System.getProperty(WORKING_DIR_PROPERTY);
        try {
            FileWriter fileWriter = new FileWriter(dir, order.getFileName(), pullServing.getFileSize());
            fileWriter.write(socketChannel);
            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == pullServing.getFileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
