//package com.sushi.components.server;
//
//import com.sushi.components.common.file.FileSender;
//import com.sushi.components.common.serving.SushiServing;
//
//import java.nio.ByteBuffer;
//import java.nio.channels.AsynchronousSocketChannel;
//import java.nio.channels.CompletionHandler;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//
//public class FileServingService {
//
//    private final AsynchronousSocketChannel socketChannel;
//
//    public FileServingService(AsynchronousSocketChannel socketChannel) {
//        this.socketChannel = socketChannel;
//    }
//
//    public void sendServing(SushiServing serving, Path path) {
//        OnComplete onComplete = FileSender::transferFile;
//
//        final ByteBuffer buffer = ByteBuffer.wrap(serving.toRequest().getBytes(StandardCharsets.UTF_8));
//        socketChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {
//            @Override
//            public void completed(Integer result, Void attachment) {
//                while (buffer.hasRemaining()) {
//                    socketChannel.write(buffer, null, this);
//                }
//                onComplete.done(socketChannel, path);
//            }
//
//            @Override
//            public void failed(Throwable exc, Void attachment) {
//                exc.printStackTrace();
//            }
//        });
//    }
//
//}
