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
            System.out.println(sushiPullServing.toRequest());
            receiveFile(socketChannel, sushiPullServing, sushiOrder);

            return null;
//            return new SushiPullServing(SushiServingStatus.OK);
        } catch (IOException ioe) {
            System.out.println("TODO");
        }
        throw new RuntimeException("");
    }

    private void receiveFile(SocketChannel socketChannel, SushiPullServing sushiPullServing, SushiPullOrder sushiOrder) throws IOException {

        FileChannel open = FileChannel.open(Paths.get("/tmp/output", sushiOrder.getFileName()), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        long position = 0L;
        while (position < sushiPullServing.getFileSize()) {
            System.out.println(position);
            position += open.transferFrom(socketChannel, position, Constants.TRANSFER_MAX_SIZE);
        }
        System.out.println(position);
        open.close();

    }

    private String readResponse(SocketChannel socketChannel) throws IOException {
        StringBuilder response = new StringBuilder();

        while (!response.toString().contains("status")) {
            System.out.println(response);
            final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
            final long bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                response.append(new String(buffer.array()));
            }
        }
        System.out.println("BYE");
        return response.toString();
    }
}
