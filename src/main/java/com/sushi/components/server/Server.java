package com.sushi.components.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Server {

    private static final int PORT = 9999;

    private final AsynchronousServerSocketChannel server;
    private final AsynchronousChannelGroup group;
    private final RequestService requestService;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        while (true) {
            server.start();
            System.in.read();
        }
    }

    public Server() {
        try {
            this.group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(2));
            this.server = AsynchronousServerSocketChannel.open(this.group).bind(new InetSocketAddress(PORT));
            this.requestService = new RequestService();
        } catch (IOException e) {
            throw new IllegalStateException("unable to start FileReceiver", e);
        }
    }

    public void start() {
        accept();
    }

    private void accept() {
        this.server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(final AsynchronousSocketChannel channel, final Void attachment) {
                //for next call
                accept();

                requestService.handleRequest(channel);
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
