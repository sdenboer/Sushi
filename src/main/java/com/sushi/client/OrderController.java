package com.sushi.client;

import com.sushi.client.file.FileOrderService;
import com.sushi.client.pull.PullOrderService;
import com.sushi.client.push.PushOrderService;
import com.sushi.client.remove.RemoveOrderService;
import com.sushi.client.status.StatusOrderService;
import com.sushi.components.common.configuration.SSLConfiguration;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.serving.Serving;
import tlschannel.ClientTlsChannel;
import tlschannel.TlsChannel;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

import static com.sushi.components.utils.Constants.TLS_PORT;

public class OrderController {

    public Serving handleOrder(Order order) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(order.getHost().port()));
            if (order.getHost().port() == TLS_PORT) {
                SSLContext sslContext = SSLConfiguration.authenticatedContext();
                try (TlsChannel tlsChannel = ClientTlsChannel.newBuilder(socketChannel, sslContext).build()) {
                    return sendOrder(tlsChannel, order);
                }
            } else {
                return sendOrder(socketChannel, order);
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem connecting to socket", e);
        }
    }

    private Serving sendOrder(ByteChannel byteChannel, Order order) throws IOException {
        return (switch (order.getMethod()) {
            case FILE -> new FileOrderService();
            case PULL -> new PullOrderService();
            case PUSH -> new PushOrderService();
            case REMOVE -> new RemoveOrderService();
            case STATUS -> new StatusOrderService();
        }).send(byteChannel, order);

    }
}
