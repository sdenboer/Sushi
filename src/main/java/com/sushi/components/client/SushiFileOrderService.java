package com.sushi.components.client;

import com.sushi.components.common.mappers.SushiFileServingMapper;
import com.sushi.components.common.message.order.SushiFileOrder;
import com.sushi.components.common.message.serving.SushiFileServing;
import com.sushi.components.common.senders.TextSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.message.serving.SushiServingStatus.OK;

public class SushiFileOrderService implements SushiOrderService<SushiFileOrder, SushiFileServing> {

    @Override
    public SushiFileServing send(SushiFileOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new TextSender().send(socketChannel, sushiOrder.toRequest());
            String serving = receiveServing(socketChannel);
            SushiFileServing sushiFileServing = new SushiFileServingMapper().from(serving);
            if (sushiFileServing.getSushiServingStatus().equals(OK)) {
                readPayload(socketChannel, sushiFileServing);
            }
            return sushiFileServing;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    private void readPayload(SocketChannel socketChannel, SushiFileServing sushiFileServing) {

//        try {
//            FileWriter fileWriter = new FileWriter("/home/pl00cc/tmp/output", sushiOrder.getFileName(), sushiPullServing.getFileSize());
//            fileWriter.write(socketChannel);
//            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == sushiPullServing.getFileSize()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
