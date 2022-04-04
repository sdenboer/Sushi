package com.sushi.components.error;

import com.sushi.components.error.exceptions.SushiException;
import com.sushi.components.senders.MessageSender;

import java.nio.channels.AsynchronousByteChannel;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final AsynchronousByteChannel socketChannel;

    public GlobalExceptionHandler(AsynchronousByteChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    //    private static Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof SushiException status) {
            ErrorServing serving = new ErrorServing(status.getStatus(), status.getOrderId());
            new MessageSender().send(socketChannel, serving);
        }
        e.printStackTrace();

//        LOGGER.info("Unhandled exception caught!");
    }
}


