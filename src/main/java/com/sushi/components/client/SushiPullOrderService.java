package com.sushi.components.client;

import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
            String test = readResponse(socketChannel);
            SushiPullServing sushiPullServing = SushiPullServing.fromRequest(test);
            if (sushiPullServing.getSushiServingStatus().equals(OK)) {
                receiveFile(socketChannel, sushiPullServing, sushiOrder);
            }

            return sushiPullServing;
        } catch (IOException ioe) {
            System.out.println("TODO");
        }
        throw new RuntimeException("");
    }

    private void receiveFile(SocketChannel socketChannel, SushiPullServing sushiPullServing, SushiPullOrder sushiOrder) throws IOException {

        try (FileChannel fileChannel = FileChannel.open(Paths.get("/home/pl00cc/tmp/output", sushiOrder.getFileName()), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            long position = 0L;
            while (position < sushiPullServing.getFileSize()) {
                position += fileChannel.transferFrom(socketChannel, position, Constants.TRANSFER_MAX_SIZE);
            }
            System.out.println(position == sushiPullServing.getFileSize());
        }
    }

    private String readResponse(SocketChannel socketChannel) throws IOException {
        StringBuilder response = new StringBuilder();

        while (!response.toString().contains("status")) {
            final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
            final long bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                response.append(new String(buffer.array()));
            }
        }
        return response.toString();
    }
}
