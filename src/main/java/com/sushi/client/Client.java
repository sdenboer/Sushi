package com.sushi.client;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.protocol.file.FileOrder;

import java.util.UUID;

import static com.sushi.components.utils.Constants.TLS_PORT;

public class Client {


    public static void main(String[] args) {

        FileOrder sushiOrder = FileOrder.builder()
                .host("localhost")
                .port(TLS_PORT)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/output")
                .fileName("test.txt")
                .build();
        Serving serving = new OrderController().handleOrder(sushiOrder);
    }
}
