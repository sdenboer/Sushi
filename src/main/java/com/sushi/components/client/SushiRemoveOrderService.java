package com.sushi.components.client;

import com.sushi.components.common.mappers.SushiRemoveServingMapper;
import com.sushi.components.common.message.order.SushiRemoveOrder;
import com.sushi.components.common.message.serving.SushiRemoveServing;
import com.sushi.components.common.senders.TextSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class SushiRemoveOrderService implements SushiOrderService<SushiRemoveOrder, SushiRemoveServing> {
    @Override
    public SushiRemoveServing send(SushiRemoveOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new TextSender().send(socketChannel, sushiOrder.toRequest());
            String serving = receiveServing(socketChannel);
            return new SushiRemoveServingMapper().from(serving);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
