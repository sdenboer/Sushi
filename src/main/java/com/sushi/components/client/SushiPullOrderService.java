package com.sushi.components.client;

import com.sushi.components.common.file.FileWriter;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class SushiPullOrderService extends SushiFileOrderService implements SushiOrderService<SushiPullOrder, SushiPullServing> {

    public SushiPullOrderService(String srcPath) {
        super(srcPath);
    }

    @Override
    public SushiPullServing send(SushiPullOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            write(socketChannel, sushiOrder);
//            String test = readResponse(fileChannel, socketChannel);
            return new SushiPullServing(SushiServingStatus.OK);
        } catch (IOException ioe) {
            System.out.println("TODO");
        }
        throw new RuntimeException("");
    }

    private void receiveFile(FileChannel fileChannel, SocketChannel socketChannel) throws IOException {
        long position = 0L;
        long size = fileChannel.size();
        while (position < size) {
            position += fileChannel.transferFrom(socketChannel, Constants.TRANSFER_MAX_SIZE, position);
        }
    }
}
