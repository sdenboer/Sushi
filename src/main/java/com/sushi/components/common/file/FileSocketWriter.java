//package com.sushi.components.common.file;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.AsynchronousSocketChannel;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class FileSocketWriter {
//
//    private final AsynchronousSocketChannel socketChannel;
//    private final AtomicLong position;
//    private final long fileSize;
//
//    public FileSocketWriter(AsynchronousSocketChannel socketChannel, AtomicLong position, long fileSize) {
//        this.socketChannel = socketChannel;
//        this.position = position;
//        this.fileSize = fileSize;
//    }
//
//    public int write(final ByteBuffer buffer, long position) throws IOException {
//        int bytesWritten = 0;
//        while (buffer.hasRemaining()) {
//            bytesWritten += this.socketChannel.write(buffer, position + bytesWritten);
//        }
//
//        return bytesWritten;
//    }
//
//
//
//    public AtomicLong getPosition() {
//        return position;
//    }
//
//    public boolean done() {
//        return this.position.get() == this.fileSize;
//    }
//
//    public void close() throws IOException {
//        this.fileChannel.close();
//    }
//
//}
