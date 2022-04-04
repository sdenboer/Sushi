package com.sushi.server;

import com.sushi.components.error.GlobalExceptionHandler;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

public class AsyncServerSocketChannelHandler extends ServerSocketChannelHandler {

    private final AsynchronousChannelGroup group;

    public AsyncServerSocketChannelHandler(int port) {
        super(port);
        try {
            this.group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(2));
        } catch (IOException e) {
            throw new IllegalStateException("unable to start FileReceiver", e);
        }
    }

    @Override
    public void listen() throws IOException {
        System.out.println("Waiting for connection...");
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(group);
        serverSocketChannel.bind(inetSocketAddress);
        while (true) {
            accept(serverSocketChannel);
            System.in.read();
        }
    }

    private void accept(AsynchronousServerSocketChannel serverSocketChannel) {

        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(final AsynchronousSocketChannel channel, final Void attachment) {
                //for next call
                accept(serverSocketChannel);

                new OrderInterceptor().intercept(channel);

                GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(channel);
                Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
            }

            public void failed(final Throwable exc, final Void attachment) {
                throw new RuntimeException("unable to accept new connection", exc);
            }
        });
    }

}
