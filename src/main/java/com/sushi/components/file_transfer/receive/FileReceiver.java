//package com.sushi.components.file_transfer.receive;
//
//import com.sushi.components.utils.Constants;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.AsynchronousChannelGroup;
//import java.nio.channels.AsynchronousServerSocketChannel;
//import java.nio.channels.AsynchronousSocketChannel;
//import java.nio.channels.CompletionHandler;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class FileReceiver {
//
//    private final AsynchronousServerSocketChannel server;
//    private final AsynchronousChannelGroup group;
//    private final OnComplete onComplete;
//
//    public FileReceiver(int port, int poolSize, OnComplete onComplete) {
//        this.onComplete = onComplete;
//        try {
//            this.group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(poolSize));
//            this.server = AsynchronousServerSocketChannel.open(this.group).bind(new InetSocketAddress(port));
//        } catch (IOException e) {
//            throw new IllegalStateException("unable to start FileReceiver", e);
//        }
//    }
//
//    public void start() {
//        accept();
//    }
//
//    public void stop(long wait) {
//
//        try {
//            this.group.shutdown();
//            this.group.awaitTermination(wait, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException("unable to stop FileReceiver", e);
//        }
//    }
//
//    private void handleRequest(final AsynchronousSocketChannel channel) {
//
//        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
//        channel.read(buffer, new StringBuffer(), new RequestCompletionHandler(channel, buffer, onComplete));
//    }
//
//
//    private void accept() {
//        this.server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
//            public void completed(final AsynchronousSocketChannel channel, final Void attachment) {
//                //For next call
//                accept();
//
//                //Hande request
//                handleRequest(channel);
//            }
//
//            public void failed(final Throwable exc, final Void attachment) {
//                throw new RuntimeException("unable to accept new connection", exc);
//            }
//        });
//    }
//
//
//}
