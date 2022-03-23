package com.sushi.components.client;

import com.sushi.components.common.file.FileSender;
import com.sushi.components.common.push.SushiPushOrder;
import com.sushi.components.common.push.SushiPushServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
            String response = readResponse(socketChannel);
            return SushiPushServing.fromRequest(response);
        } catch (IOException ioe) {
            System.out.println("TODO");
        }
        throw new RuntimeException("");
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
