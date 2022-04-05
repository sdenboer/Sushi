package com.sushi.client;

import com.sushi.components.error.exceptions.CheckedSushiException;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.protocol.file.FileOrder;
import com.sushi.components.protocol.status.StatusOrder;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.UUID;

import static com.sushi.client.CommandLineOptions.*;
import static com.sushi.components.utils.Constants.DEFAULT_PORT;
import static com.sushi.components.utils.Constants.TLS_PORT;

public class Client {

    public static void main(String[] args) {

        Options options = new Options();
        options.addOption(listMethod);
        options.addOption(fetchMethod);
        options.addOption(backupMethod);
        options.addOption(verifyMethod);
        options.addOption(removeMethod);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            if (args.length <= 0) {
                throw new CheckedSushiException("No arguments given");
            }

            String[] method = {args[0]};
            String[] methodArgs = Arrays.copyOfRange(args, 1, args.length);

            CommandLine cmd = parser.parse(options, method);

            OrderOption orderOption = new OrderOptionFactory(cmd).getOrderOption();
            options = orderOption.getMethodOptions();

            cmd = parser.parse(options, methodArgs);

            Order order = orderOption.createOrder(cmd);
            Serving serving = new OrderController().handleOrder(order);
            System.out.println(serving.getServingStatus());

        } catch (ParseException | CheckedSushiException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("options", options);
            System.exit(1);
        }


//        Order order = getOrder();
//        Serving serving = new OrderController().handleOrder(order);
//        System.out.println(serving.getServingStatus());
//
//        new Thread(() -> System.out.println(new OrderController().handleOrder(getOrder()).getServingStatus())).start();
//        new Thread(() -> System.out.println(new OrderController().handleOrder(getOrder()).getServingStatus())).start();
//        new Thread(() -> System.out.println(new OrderController().handleOrder(getOtherOrder()).getServingStatus())).start();
//        new Thread(() -> System.out.println(new OrderController().handleOrder(order).getServingStatus())).start();
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
