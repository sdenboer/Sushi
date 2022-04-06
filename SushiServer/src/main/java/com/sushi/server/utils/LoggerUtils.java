package com.sushi.server.utils;

public class LoggerUtils {

    public static String createMessage(OrderContext orderContext) {
        return "Order: " + orderContext.orderId() + " - ";
    }
}
