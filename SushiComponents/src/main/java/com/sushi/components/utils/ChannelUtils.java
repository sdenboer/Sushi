package com.sushi.components.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.Arrays;

public class ChannelUtils {

    private static final Logger logger = Logger.getLogger(ChannelUtils.class);

    public static void close(Channel... channels) {
        Arrays.stream(channels).forEach(channel -> {
            try {
                channel.close();
            } catch (IOException e) {
                logger.error("Unable to close " + channel.getClass().getSimpleName() + " channel", e);
            }
        });

    }
}
