package com.sushi.server.utils;

import com.sushi.components.utils.OrderContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerUtils {

    public static String createMessage(OrderContext orderContext) {
        return "Order: " + orderContext.orderId() + " - ";
    }
}
