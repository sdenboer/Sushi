package com.sushi.components.common.error;

import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.server.TextServingResponse;

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
            ErrorServing errorServing = new ErrorServing(status.getStatus(), status.getOrderId());
            new TextServingResponse(socketChannel).sendServing(errorServing);
        }
        e.printStackTrace();

//        LOGGER.info("Unhandled exception caught!");
        System.out.println("HELLO");
    }
}


