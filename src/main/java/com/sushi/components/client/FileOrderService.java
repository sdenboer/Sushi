package com.sushi.components.client;

import com.sushi.components.common.protocol.file.FileOrder;
import com.sushi.components.common.protocol.file.FileServing;
import com.sushi.components.common.protocol.file.FileServingMapper;
import com.sushi.components.common.senders.TextSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.sushi.components.common.message.serving.ServingStatus.OK;

public class FileOrderService implements OrderService<FileOrder, FileServing> {

    @Override
    public FileServing send(FileOrder sushiOrder) {
        InetSocketAddress hostAddress = new InetSocketAddress(sushiOrder.getHost().host(), sushiOrder.getHost().port());
        try (SocketChannel socketChannel = SocketChannel.open(hostAddress)) {

            new TextSender().send(socketChannel, sushiOrder.toRequest());
            String serving = receiveServing(socketChannel);
            FileServing fileServing = new FileServingMapper().from(serving);
            if (fileServing.getSushiServingStatus().equals(OK)) {
                String response = receiveTextPayload(socketChannel, fileServing.getPayloadSize());
                System.out.println(response);
            }
            return fileServing;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
