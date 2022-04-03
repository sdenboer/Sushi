package com.sushi.components.client.pull;

import com.sushi.components.client.OrderService;
import com.sushi.components.common.FileWriter;
import com.sushi.components.common.protocol.pull.PullOrder;
import com.sushi.components.common.protocol.pull.PullServing;
import com.sushi.components.common.protocol.pull.PullServingMapper;
import com.sushi.components.common.senders.MessageSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.message.serving.ServingStatus.OK;
import static com.sushi.components.utils.Constants.TMP_DIR;

public class PullOrderService implements OrderService<PullOrder, PullServing> {

    @Override
    public PullServing send(PullOrder order) {
        InetSocketAddress hostAddress = new InetSocketAddress(order.getHost().host(), order.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new MessageSender().send(socketChannel, order);
            String serving = receiveServing(socketChannel);
            PullServing pullServing = new PullServingMapper().from(serving);
            if (pullServing.getServingStatus().equals(OK)) {
                receiveFilePayload(socketChannel, pullServing, order);
            }
            return pullServing;
        } catch (IOException e) {
            throw new RuntimeException("Cannot connect to socket");
        }
    }

    private void receiveFilePayload(SocketChannel socketChannel, PullServing pullServing, PullOrder order) {

        try {
            FileWriter fileWriter = new FileWriter(TMP_DIR, order.getFileName(), pullServing.getFileSize());
            fileWriter.write(socketChannel);
            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == pullServing.getFileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
