package com.sushi.client;

import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.protocol.file.FileOrder;
import com.sushi.components.protocol.status.StatusOrder;

import java.util.UUID;

import static com.sushi.components.utils.Constants.DEFAULT_PORT;
import static com.sushi.components.utils.Constants.TLS_PORT;

public class Client {

    public static void main(String[] args) {


        Order order = getOrder();
        Serving serving = new OrderController().handleOrder(order);
        System.out.println(serving.getServingStatus());

        new Thread(() -> System.out.println(new OrderController().handleOrder(getOrder()).getServingStatus())).start();
        new Thread(() -> System.out.println(new OrderController().handleOrder(getOrder()).getServingStatus())).start();
        new Thread(() -> System.out.println(new OrderController().handleOrder(getOtherOrder()).getServingStatus())).start();
        new Thread(() -> System.out.println(new OrderController().handleOrder(order).getServingStatus())).start();
    }

    private static Order getOrder() {
        return FileOrder.builder()
                .host("localhost")
                .port(TLS_PORT)
                .orderId(UUID.randomUUID())
                .dir("/tmp/input")
                .fileName("test.txt")
                .build();
    }

    private static Order getOtherOrder() {
        return StatusOrder.builder()
                .host("localhost")
                .port(DEFAULT_PORT)
                .orderId(UUID.randomUUID())
                .build();
    }
}
