package com.sushi.server.handlers;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

public class AsyncServerSocketChannelHandler extends ServerSocketChannelHandler {

    private static final Logger logger = Logger.getLogger(AsyncServerSocketChannelHandler.class);

    private final AsynchronousChannelGroup group;

    public AsyncServerSocketChannelHandler(int port) {
        super(port);
        try {
            this.group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(10));
        } catch (IOException e) {
            throw new IllegalStateException("unable to start FileReceiver", e);
        }
    }

    @Override
    public void listen() throws IOException {
        logger.info("Waiting for connection...");
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(group);
        serverSocketChannel.bind(inetSocketAddress);
        accept(serverSocketChannel);
    }

    private void accept(AsynchronousServerSocketChannel serverSocketChannel) {

        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

            @Override
            public void completed(final AsynchronousSocketChannel channel, final Void attachment) {
                serverSocketChannel.accept(null, this);

                new OrderInterceptor().intercept(channel);
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
                throw new RuntimeException("unable to accept new connection", exc);
            }
        });
    }

}
