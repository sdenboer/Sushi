package com.sushi.components.common.error;

import com.sushi.components.common.error.exceptions.SushiException;
import com.sushi.components.common.senders.SushiMessageSender;

import java.nio.channels.AsynchronousSocketChannel;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final AsynchronousSocketChannel socketChannel;

    public GlobalExceptionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    //    private static Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof SushiException status) {
            ErrorServing serving = new ErrorServing(status.getStatus(), status.getOrderId());
            new SushiMessageSender().send(socketChannel, serving);
        }
        e.printStackTrace();

//        LOGGER.info("Unhandled exception caught!");
    }
}


