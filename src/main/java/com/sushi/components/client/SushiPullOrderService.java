package com.sushi.components.client;

import com.sushi.components.common.file.FileWriter;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.serving.SushiServingStatus.OK;

public class SushiPullOrderService extends SushiFileOrderService implements SushiOrderService<SushiPullOrder, SushiPullServing> {

    public SushiPullOrderService(String srcPath) {
        super(srcPath);
    }

    @Override
    public SushiPullServing send(SushiPullOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            write(socketChannel, sushiOrder);
            String test = readServing(socketChannel);
            SushiPullServing sushiPullServing = SushiPullServing.fromRequest(test);
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
            System.out.println(fileWriter.getPosition().get() == sushiPullServing.getFileSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
