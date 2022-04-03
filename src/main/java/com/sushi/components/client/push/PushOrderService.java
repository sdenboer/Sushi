package com.sushi.components.client.push;

import com.sushi.components.client.OrderService;
import com.sushi.components.common.protocol.push.PushOrder;
import com.sushi.components.common.protocol.push.PushServing;
import com.sushi.components.common.protocol.push.PushServingMapper;
import com.sushi.components.common.senders.MessageSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class PushOrderService implements OrderService<PushOrder, PushServing> {

    @Override
    public PushServing send(PushOrder order) {
        InetSocketAddress hostAddress = new InetSocketAddress(order.getHost().host(), order.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new MessageSender().send(socketChannel, order);
            String response = receiveServing(socketChannel);
            return new PushServingMapper().from(response);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

}
