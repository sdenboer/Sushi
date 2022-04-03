package com.sushi.components.client;

import com.sushi.components.common.protocol.remove.RemoveOrder;
import com.sushi.components.common.protocol.remove.RemoveServing;
import com.sushi.components.common.protocol.remove.RemoveServingMapper;
import com.sushi.components.common.senders.TextSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class RemoveOrderService implements OrderService<RemoveOrder, RemoveServing> {
    @Override
    public RemoveServing send(RemoveOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new TextSender().send(socketChannel, sushiOrder.toRequest());
            String serving = receiveServing(socketChannel);
            return new RemoveServingMapper().from(serving);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
