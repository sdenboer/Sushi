package com.sushi.server.utils;

import com.sushi.server.OrderContext;

public class LoggerUtils {

    public static String createMessage(OrderContext orderContext) {
        return "Order: " + orderContext.orderId() + " - ";
    }
}
