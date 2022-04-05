package com.sushi.client;

import com.sushi.components.error.exceptions.CheckedSushiException;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import org.apache.commons.cli.*;

import java.util.Arrays;

import static com.sushi.client.CommandLineOptions.*;

public class CommandLineHandler {

    public static void parse(String[] args) {
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
    }
}
