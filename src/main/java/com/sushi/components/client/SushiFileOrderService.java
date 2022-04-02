package com.sushi.components.client;

import com.sushi.components.common.file.SushiFileOrder;
import com.sushi.components.common.file.SushiFileServing;
import com.sushi.components.common.file.mappers.SushiFileServingMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class SushiFileOrderService implements SushiOrderService<SushiFileOrder, SushiFileServing> {

    @Override
    public SushiFileServing send(SushiFileOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            write(socketChannel, sushiOrder);
            String response = readServing(socketChannel);
            return new SushiFileServingMapper().from(response);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
