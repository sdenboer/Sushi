package com.sushi.components.client.status;

import com.sushi.components.client.OrderService;
import com.sushi.components.common.protocol.status.StatusOrder;
import com.sushi.components.common.protocol.status.StatusServing;
import com.sushi.components.common.protocol.status.StatusServingMapper;
import com.sushi.components.common.senders.TextSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.message.serving.ServingStatus.OK;

public class StatusOrderService implements OrderService<StatusOrder, StatusServing> {

    @Override
    public StatusServing send(StatusOrder order) {
        InetSocketAddress hostAddress = new InetSocketAddress(order.getHost().host(), order.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new TextSender().send(socketChannel, order.toRequest());
            String serving = receiveServing(socketChannel);
            StatusServing statusServing = new StatusServingMapper().from(serving);
            if (statusServing.getServingStatus().equals(OK)) {
                String response = receiveTextPayload(socketChannel, statusServing.getPayloadSize());
                System.out.println(response);
            }
            return statusServing;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
