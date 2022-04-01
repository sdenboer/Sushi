package com.sushi.components.client;

import com.sushi.components.common.file.FileSender;
import com.sushi.components.common.push.SushiPushOrder;
import com.sushi.components.common.push.SushiPushServing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class SushiPushOrderService extends SushiFileOrderService implements SushiOrderService<SushiPushOrder, SushiPushServing> {

    public SushiPushOrderService(String srcPath) {
        super(srcPath);
    }

    @Override
    public SushiPushServing send(SushiPushOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            write(socketChannel, sushiOrder);
            FileSender.transferFile(socketChannel, srcPath);
            String response = readServing(socketChannel);
            return SushiPushServing.fromRequest(response);
        } catch (IOException ioe) {
            System.out.println("TODO");
        }
        throw new RuntimeException("");
    }

}
