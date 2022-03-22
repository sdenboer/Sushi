//package com.sushi.components.file_transfer.receive;
//
//import com.sushi.components.protocol.order.SushiOrderWrapper;
//import com.sushi.components.protocol.order.SushiOrderWrapperField;
//import com.sushi.components.utils.Constants;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.AsynchronousChannelGroup;
//import java.nio.channels.AsynchronousServerSocketChannel;
//import java.nio.channels.AsynchronousSocketChannel;
//import java.nio.channels.CompletionHandler;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.Executors;
//import java.util.stream.Collectors;
//
//import static com.sushi.components.protocol.order.SushiOrderMethod.PUSH;
//import static java.util.function.Predicate.not;
//
//public class MessageReceiver {
//
//    private final AsynchronousServerSocketChannel server;
//    private final AsynchronousChannelGroup group;
//    private final OnComplete onComplete;
//
//    public MessageReceiver(int port, int poolSize, OnComplete onComplete) {
//        this.onComplete = onComplete;
//        try {
//            this.group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(poolSize));
//            this.server = AsynchronousServerSocketChannel.open(this.group).bind(new InetSocketAddress(port));
//        } catch (IOException e) {
//            throw new IllegalStateException("unable to start FileReceiver", e);
//        }
//    }
//
//    private void handleRequest(final AsynchronousSocketChannel channel) {
//        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
//
//        channel.read(buffer, new StringBuffer(), new CompletionHandler<>() {
//            @Override
//            public void completed(final Integer result, final StringBuffer attachment) {
//                if (result < 0) {
//                    close(channel, null);
//                    return;
//                }
//                attachment.append(new String(buffer.array()).trim());
//
//
//                if (isPushMethod(attachment)) {
//                    String[] split = attachment.toString().split("\n");
//                    Map<SushiOrderWrapperField, String> sushiOrderWrappers = Arrays.stream(split)
//                            .takeWhile(not(String::isEmpty))
//                            .map(l -> l.split(": "))
//                            .map(header -> {
//                                SushiOrderWrapperField sushiOrderWrapperField = SushiOrderWrapperField.fromString(header[0]);
//                                String value = header[1];
//                                return new SushiOrderWrapper(sushiOrderWrapperField, value);
//                            }).collect(Collectors.toMap(SushiOrderWrapper::key, SushiOrderWrapper::value));
//
//                    String s = sushiOrderWrappers.get(SushiOrderWrapperField.FILE_SIZE);
//                    System.out.println(s);
//                    final FileMetaData fileMetaData = new FileMetaData(sushiOrderWrappers.get(SushiOrderWrapperField.FILE),
//                            Long.parseLong(sushiOrderWrappers.get(SushiOrderWrapperField.FILE_SIZE)));
//
//                    try {
//                        FileWriterProxy fileWriterProxy = new FileWriterProxy(sushiOrderWrappers.get(SushiOrderWrapperField.DIR), fileMetaData);
//                        attachment.setLength(0);
//                        read(channel, fileWriterProxy, onComplete);
//                    } catch (IOException e) {
//                        close(channel, null);
//                        throw new RuntimeException("unable to create file writer proxy", e);
//                    }
//                } else {
//                    buffer.clear();
//                    channel.read(buffer, attachment, this);
//                }
//            }
//
//
//            @Override
//            public void failed(final Throwable exc, final StringBuffer attachment) {
//                close(channel, null);
//                throw new RuntimeException("unable to read meta data", exc);
//            }
//
//            private void close(final AsynchronousSocketChannel channel, final FileWriterProxy proxy) {
//                try {
//
//                    if (!Objects.isNull(proxy)) {
//                        proxy.getFileWriter().close();
//                    }
//                    channel.close();
//                } catch (IOException e) {
//                    throw new RuntimeException("unable to close channel and FileWriter", e);
//                }
//            }
//
//            private void read(final AsynchronousSocketChannel channel, final FileWriterProxy proxy, OnComplete onComplete) {
//
//                final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
//                channel.read(buffer, proxy, new CompletionHandler<>() {
//                    @Override
//                    public void completed(final Integer result, final FileWriterProxy attachment) {
//                        if (result >= 0) {
//                            if (result > 0) {
//                                writeToFile(channel, buffer, attachment);
//                            }
//
//                            buffer.clear();
//                            channel.read(buffer, attachment, this);
//                        } else if (attachment.done()) {
//                            onComplete(attachment);
//                            close(channel, attachment);
//                        }
//                    }
//
//
//                    @Override
//                    public void failed(final Throwable exc, final FileWriterProxy attachment) {
//                        throw new RuntimeException("unable to read data", exc);
//                    }
//
//                    private void writeToFile(final AsynchronousSocketChannel channel, final ByteBuffer buffer, final FileWriterProxy proxy) {
//
//                        try {
//                            buffer.flip();
//
//                            final long bytesWritten = proxy.getFileWriter().write(buffer, proxy.getPosition().get());
//                            proxy.getPosition().addAndGet(bytesWritten);
//                        } catch (IOException e) {
//                            close(channel, proxy);
//                            throw new RuntimeException("unable to write bytes to file", e);
//                        }
//                    }
//
//                    private void onComplete(final FileWriterProxy proxy) {
//
//                        onComplete.onComplete(proxy);
//                    }
//                });
//            }
//        });
//    }
//
//
//    private boolean isPushMethod(StringBuffer buffer) {
//        return buffer.toString().contains(PUSH.getValue());
//    }
//}
//
//
//
//
//
