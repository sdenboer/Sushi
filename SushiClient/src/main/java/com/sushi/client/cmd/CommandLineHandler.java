package com.sushi.client.cmd;

import com.sushi.client.exceptions.SushiException;
import com.sushi.client.order.OrderController;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import org.apache.commons.cli.*;

import java.util.Arrays;

import static com.sushi.client.cmd.CommandLineOptions.*;

public class CommandLineHandler {

    private final OrderController orderController;

    public CommandLineHandler() {
        this.orderController = new OrderController();
    }

    public CommandLineHandler(OrderController orderController) {
        this.orderController = orderController;
    }

    public ServingStatus parse(String[] args) {
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
                throw new SushiException();
            }

            String[] method = {args[0]};
            String[] methodArgs = Arrays.copyOfRange(args, 1, args.length);

            CommandLine cmd = parser.parse(options, method);

            OrderOption orderOption = new OrderOptionFactory(cmd).getOrderOption();
            options = orderOption.getMethodOptions();

            cmd = parser.parse(options, methodArgs);

            Order order = orderOption.createOrder(cmd);
            Serving serving = orderController.handleOrder(order);
            System.out.println(serving.getServingStatus());
            return serving.getServingStatus();

        } catch (ParseException | SushiException e) {
            formatter.printHelp("options", options);
            System.exit(1);
        }
        return null;
    }
}
