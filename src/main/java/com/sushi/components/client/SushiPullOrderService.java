package com.sushi.components.client;

import com.sushi.components.common.file_transfer.FileWriter;
import com.sushi.components.common.mappers.SushiPullServingMapper;
import com.sushi.components.common.message.order.SushiPullOrder;
import com.sushi.components.common.message.serving.SushiPullServing;
import com.sushi.components.common.senders.SushiMessageSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.message.serving.SushiServingStatus.OK;

public class SushiPullOrderService implements SushiOrderService<SushiPullOrder, SushiPullServing> {

    private static final String DIR = "/home/pl00cc/tmp/output";

    @Override
    public SushiPullServing send(SushiPullOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new SushiMessageSender().send(socketChannel, sushiOrder);
            String serving = receiveServing(socketChannel);
            SushiPullServing sushiPullServing = new SushiPullServingMapper().from(serving);
            if (sushiPullServing.getSushiServingStatus().equals(OK)) {
                receiveFilePayload(socketChannel, sushiPullServing, sushiOrder);
            }
            return sushiPullServing;
        } catch (IOException e) {
            throw new RuntimeException("Cannot connect to socket");
        }
    }

    private void receiveFilePayload(SocketChannel socketChannel, SushiPullServing sushiPullServing, SushiPullOrder sushiOrder) {

        try {
            FileWriter fileWriter = new FileWriter(DIR, sushiOrder.getFileName(), sushiPullServing.getFileSize());
            fileWriter.write(socketChannel);
            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == sushiPullServing.getFileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
