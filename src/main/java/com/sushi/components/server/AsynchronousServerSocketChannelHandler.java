package com.sushi.components.server;

import com.sushi.components.common.error.GlobalExceptionHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsynchronousServerSocketChannelHandler {

    private static final int PORT = 9999;

    private final AsynchronousServerSocketChannel channel;
    private final AsynchronousChannelGroup group;

    public AsynchronousServerSocketChannelHandler() {
        try {
            this.group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(2));
            this.channel = AsynchronousServerSocketChannel.open(this.group).bind(new InetSocketAddress(PORT));
        } catch (IOException e) {
            throw new IllegalStateException("unable to start FileReceiver", e);
        }
    }

    public void listen() {
        this.channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(final AsynchronousSocketChannel channel, final Void attachment) {
                //for next call
                listen();

                OrderController orderController = new OrderController(channel);
                orderController.handleOrder();

                GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(channel);
                Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
            }

            public void failed(final Throwable exc, final Void attachment) {
                throw new RuntimeException("unable to accept new connection", exc);
            }
        });
    }

    public void stop(long wait) {

        try {
            this.group.shutdown();
            this.group.awaitTermination(wait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("unable to stop FileReceiver", e);
        }
    }

}
