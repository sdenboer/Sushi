package com.sushi.components.server;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

public class FileServingService  {

    private final AsynchronousSocketChannel socketChannel;

    public FileServingService(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void sendServing(SushiServing serving) {

        try (FileChannel fileChannel = FileChannel.open(Paths.get("/tmp/output/input2"), StandardOpenOption.READ)) {
            File tf = Paths.get("").toFile();
            long length = tf.length();
            final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
            AtomicLong atomicLong = new AtomicLong(0L);
            long position = fileChannel.read(buffer, 0L);

            this.socketChannel.write(buffer, fileChannel, new CompletionHandler<>() {
                @Override
                public void completed(Integer result, FileChannel attachment) {
                    if (result > 0) {
                        buffer.flip();
                        try {
                            atomicLong.addAndGet(fileChannel.read(buffer, position));
                            if (atomicLong.get() == length) {
                                System.out.println("DONE");
                            } else {
                                socketChannel.write(buffer, fileChannel, this);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, FileChannel attachment) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
