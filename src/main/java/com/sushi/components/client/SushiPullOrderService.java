package com.sushi.components.client;

import com.sushi.components.common.file_transfer.FileWriter;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.pull.mappers.SushiPullServingMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.serving.SushiServingStatus.OK;

public class SushiPullOrderService implements SushiOrderService<SushiPullOrder, SushiPullServing> {

    private final String srcPath;

    public SushiPullOrderService(String srcPath) {
        this.srcPath = srcPath;
    }

    @Override
    public SushiPullServing send(SushiPullOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            write(socketChannel, sushiOrder);
            String serving = readServing(socketChannel);
            SushiPullServing sushiPullServing = new SushiPullServingMapper().from(serving);
            if (sushiPullServing.getSushiServingStatus().equals(OK)) {
                receiveFile(socketChannel, sushiPullServing, sushiOrder);
            }

            return sushiPullServing;
        } catch (IOException e) {
            throw new RuntimeException("Cannot connect to socket");
        }
    }

    private void receiveFile(SocketChannel socketChannel, SushiPullServing sushiPullServing, SushiPullOrder sushiOrder) {

        try {
            FileWriter fileWriter = new FileWriter("/home/pl00cc/tmp/output", sushiOrder.getFileName(), sushiPullServing.getFileSize());
            fileWriter.write(socketChannel);
            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == sushiPullServing.getFileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
