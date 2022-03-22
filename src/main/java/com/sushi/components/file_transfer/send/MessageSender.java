//package com.sushi.components.file_transfer.send;
//
//import com.sushi.components.protocol.order.SushiOrder;
//import com.sushi.components.utils.Constants;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//import java.nio.channels.SocketChannel;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//
//public class MessageSender implements AutoCloseable {
//
//    private final SocketChannel channel;
//
//    public MessageSender(String host, final int port) throws IOException {
//        InetSocketAddress hostAddress = new InetSocketAddress(host, port);
//        this.channel = SocketChannel.open(hostAddress);
//    }
//
//    public void transferFile(SushiOrder sushiOrder, String path) throws IOException {
//        try (FileChannel fileChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ)) {
//            this.write(sushiOrder.toRequest());
//
//            long position = 0L;
//            long size = fileChannel.size();
//            while (position < size) {
//                position += fileChannel.transferTo(position, Constants.TRANSFER_MAX_SIZE, this.channel);
//            }
//        }
//    }
//
//    public void write(ByteBuffer buffer) throws IOException {
//        while (buffer.hasRemaining()) {
//            this.channel.write(buffer);
//        }
//    }
//
//    public void write(String message) throws IOException {
//        final ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
//        this.write(buffer);
//    }
//
//    public long read(ByteBuffer buffer) throws IOException {
//        return this.channel.read(buffer);
//    }
//
//    @Override
//    public void close() throws Exception {
//        this.channel.close();
//    }
//
//
//
//}
