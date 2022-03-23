package com.sushi.components.utils;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class ChannelUtils {

    public static void close(AsynchronousSocketChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to close channel");
        }
    }
}
