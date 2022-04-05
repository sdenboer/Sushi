package com.sushi.components.error;

import com.sushi.components.error.exceptions.SushiException;
import com.sushi.components.senders.MessageSender;
import com.sushi.components.utils.ChannelUtils;

import java.nio.channels.AsynchronousByteChannel;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final AsynchronousByteChannel socketChannel;

    public GlobalExceptionHandler(AsynchronousByteChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    //    private static Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof SushiException sushiException) {
            ErrorServing serving = new ErrorServing(sushiException.getStatus(), sushiException.getOrderId());
            new MessageSender().send(socketChannel, serving);
            System.out.println("Error for order: " + sushiException.getOrderId());
        }
        e.printStackTrace();
        ChannelUtils.close(socketChannel);

//        LOGGER.info("Unhandled exception caught!");
    }
}


