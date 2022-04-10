package com.sushi.client.order;

import com.sushi.client.file.FileOrderService;
import com.sushi.client.pull.PullOrderService;
import com.sushi.client.push.PushOrderService;
import com.sushi.client.remove.RemoveOrderService;
import com.sushi.client.status.StatusOrderService;
import com.sushi.components.configuration.SSLConfiguration;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import tlschannel.ClientTlsChannel;
import tlschannel.TlsChannel;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

import static com.sushi.components.utils.Constants.TLS_PORT;

public class OrderController {

    private final int tlsPort;

    public OrderController(int tlsPort) {
        this.tlsPort = tlsPort;
    }

    public OrderController() {
        this.tlsPort = TLS_PORT;
    }

    public Serving handleOrder(Order order) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(
                    new InetSocketAddress(order.getHost().host(), order.getHost().port()));
            if (order.getHost().port() == tlsPort) {
                SSLContext sslContext = SSLConfiguration.authenticatedContext();
                try (TlsChannel tlsChannel = ClientTlsChannel.newBuilder(socketChannel, sslContext)
                        .build()) {
                    return sendOrder(tlsChannel, order);
                }
            } else {
                return sendOrder(socketChannel, order);
            }
        } catch (IOException e) {
            System.out.println("Problem connecting to socket");
            System.exit(1);
            return null;
        }
    }

    private Serving sendOrder(ByteChannel byteChannel, Order order) throws IOException {
        return (switch (order.getOrderMethod()) {
            case FILE -> new FileOrderService();
            case PULL -> new PullOrderService();
            case PUSH -> new PushOrderService();
            case REMOVE -> new RemoveOrderService();
            case STATUS -> new StatusOrderService();
        }).send(byteChannel, order);

    }
}
