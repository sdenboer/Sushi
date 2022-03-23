package com.sushi.components.utils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class CompletionHandlerUtils {
    public static boolean errorOccurred(Integer result) {
        return result < 0;
    }

    public static void resume(ByteBuffer buffer, AsynchronousSocketChannel channel, StringBuffer attachment, CompletionHandler<Integer, StringBuffer> completionHandler) {
        buffer.clear();
        channel.read(buffer, attachment, completionHandler);
    }
}
