package com.sushi.components.client;

import com.sushi.components.common.FileWriter;
import com.sushi.components.common.protocol.pull.PullOrder;
import com.sushi.components.common.protocol.pull.PullServing;
import com.sushi.components.common.protocol.pull.PullServingMapper;
import com.sushi.components.common.senders.SushiMessageSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.message.serving.ServingStatus.OK;

public class PullOrderService implements OrderService<PullOrder, PullServing> {

    private static final String DIR = "/home/pl00cc/tmp/output";

    @Override
    public PullServing send(PullOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new SushiMessageSender().send(socketChannel, sushiOrder);
            String serving = receiveServing(socketChannel);
            PullServing pullServing = new PullServingMapper().from(serving);
            if (pullServing.getSushiServingStatus().equals(OK)) {
                receiveFilePayload(socketChannel, pullServing, sushiOrder);
            }
            return pullServing;
        } catch (IOException e) {
            throw new RuntimeException("Cannot connect to socket");
        }
    }

    private void receiveFilePayload(SocketChannel socketChannel, PullServing pullServing, PullOrder sushiOrder) {

        try {
            FileWriter fileWriter = new FileWriter(DIR, sushiOrder.getFileName(), pullServing.getFileSize());
            fileWriter.write(socketChannel);
            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == pullServing.getFileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
