package com.sushi.components.client;

import com.sushi.components.common.file_transfer.FileSender;
import com.sushi.components.common.push.SushiPushOrder;
import com.sushi.components.common.push.SushiPushServing;
import com.sushi.components.common.push.mappers.SushiPushServingMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class SushiPushOrderService implements SushiOrderService<SushiPushOrder, SushiPushServing> {

    private final String srcPath;

    public SushiPushOrderService(String srcPath) {
        this.srcPath = srcPath;
    }

    @Override
    public SushiPushServing send(SushiPushOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            write(socketChannel, sushiOrder);
            FileSender.transferFile(socketChannel, srcPath);
            String response = readServing(socketChannel);
            return new SushiPushServingMapper().from(response);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

}
