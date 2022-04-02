package com.sushi.components.client;

import com.sushi.components.common.file.SushiFileOrder;
import com.sushi.components.common.file.SushiFileServing;
import com.sushi.components.common.file.mappers.SushiFileServingMapper;
import com.sushi.components.common.file_transfer.FileWriter;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.pull.mappers.SushiPullServingMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.serving.SushiServingStatus.OK;

public class SushiFileOrderService implements SushiOrderService<SushiFileOrder, SushiFileServing> {

    @Override
    public SushiFileServing send(SushiFileOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            write(socketChannel, sushiOrder);
            String serving = readServing(socketChannel);
            SushiFileServing sushiFileServing = new SushiFileServingMapper().from(serving);
            if (sushiFileServing.getSushiServingStatus().equals(OK)) {
                readPayload(socketChannel, sushiFileServing);
            }
            return sushiFileServing;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    private void readPayload(SocketChannel socketChannel, SushiFileServing sushiFileServing) {

//        try {
//            FileWriter fileWriter = new FileWriter("/home/pl00cc/tmp/output", sushiOrder.getFileName(), sushiPullServing.getFileSize());
//            fileWriter.write(socketChannel);
//            System.out.println("FILE IS SAME SIZE " + (fileWriter.getPosition().get() == sushiPullServing.getFileSize()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
