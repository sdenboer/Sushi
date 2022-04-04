package com.sushi.components.utils;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.Arrays;

public class ChannelUtils {

    public static void close(Channel... channels) {
        Arrays.stream(channels).forEach(channel -> {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to close channel");
            }
        });

    }
}
