package com.sushi.server;

import com.sushi.components.configuration.SSLConfiguration;
import com.sushi.components.error.GlobalExceptionHandler;
import tlschannel.ServerTlsChannel;
import tlschannel.async.AsynchronousTlsChannel;
import tlschannel.async.AsynchronousTlsChannelGroup;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class AsyncTlsServerSocketChannelHandler extends ServerSocketChannelHandler implements Runnable {

    private final AsynchronousTlsChannelGroup channelGroup;
    private final SSLContext sslContext;

    public AsyncTlsServerSocketChannelHandler(int port) {
        super(port);
        sslContext = SSLConfiguration.authenticatedContext();
        this.channelGroup = new AsynchronousTlsChannelGroup();
    }

    @Override
    public void listen() throws IOException {
        System.out.println("Waiting for TLS connection...");
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(inetSocketAddress);
            while (true) {
                accept(serverSocket);
            }
        }
    }

    private void accept(ServerSocketChannel serverSocket) throws IOException {
        SocketChannel socketChannel = serverSocket.accept();
        socketChannel.configureBlocking(false);
        ServerTlsChannel tlsChannel = ServerTlsChannel.newBuilder(socketChannel, sslContext).build();
        AsynchronousTlsChannel asyncTlsChannel = new AsynchronousTlsChannel(channelGroup, tlsChannel, socketChannel);

        new OrderInterceptor().intercept(asyncTlsChannel);

        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(asyncTlsChannel);
        Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
    }

}
