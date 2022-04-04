package com.sushi.client;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.protocol.file.FileOrder;

import java.util.UUID;

import static com.sushi.components.utils.Constants.DEFAULT_PORT;
import static com.sushi.components.utils.Constants.TLS_PORT;

public class Client {

    public static void main(String[] args) {

        FileOrder sushiOrder = FileOrder.builder()
                .host("localhost")
                .port(TLS_PORT)
                .orderId(UUID.randomUUID())
                .dir("/tmp/input")
                .fileName("test.txt")
                .build();
        Serving serving = new OrderController().handleOrder(sushiOrder);
        System.out.println(serving.getServingStatus());
    }
}
