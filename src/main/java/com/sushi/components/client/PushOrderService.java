package com.sushi.components.client;

import com.sushi.components.common.protocol.push.PushOrder;
import com.sushi.components.common.protocol.push.PushServing;
import com.sushi.components.common.protocol.push.PushServingMapper;
import com.sushi.components.common.senders.SushiMessageSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class PushOrderService implements OrderService<PushOrder, PushServing> {

    @Override
    public PushServing send(PushOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new SushiMessageSender().send(socketChannel, sushiOrder);
            String response = receiveServing(socketChannel);
            return new PushServingMapper().from(response);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

}
