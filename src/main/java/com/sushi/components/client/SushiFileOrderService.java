package com.sushi.components.client;

import com.sushi.components.common.protocol.file.SushiFileOrder;
import com.sushi.components.common.protocol.file.SushiFileServing;
import com.sushi.components.common.protocol.file.SushiFileServingMapper;
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
                String response = receiveTextPayload(socketChannel, sushiFileServing.getPayloadSize());
                System.out.println(response);
            }
            return sushiFileServing;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
