package com.sushi.components.client;

import com.sushi.components.common.protocol.push.SushiPushOrder;
import com.sushi.components.common.protocol.push.SushiPushServing;
import com.sushi.components.common.protocol.push.SushiPushServingMapper;
import com.sushi.components.common.senders.SushiMessageSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class SushiPushOrderService implements SushiOrderService<SushiPushOrder, SushiPushServing> {

    @Override
    public SushiPushServing send(SushiPushOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new SushiMessageSender().send(socketChannel, sushiOrder);
            String response = receiveServing(socketChannel);
            return new SushiPushServingMapper().from(response);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

}
