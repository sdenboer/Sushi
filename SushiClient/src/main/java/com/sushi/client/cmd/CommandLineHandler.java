package com.sushi.client.cmd;

import static com.sushi.client.cmd.CommandLineOptions.backupMethod;
import static com.sushi.client.cmd.CommandLineOptions.fetchMethod;
import static com.sushi.client.cmd.CommandLineOptions.listMethod;
import static com.sushi.client.cmd.CommandLineOptions.removeMethod;
import static com.sushi.client.cmd.CommandLineOptions.verifyMethod;

import com.sushi.client.exceptions.SushiException;
import com.sushi.client.order.OrderController;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandLineHandler {

    public static ServingStatus parse(String[] args) {
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
                System.out.println("No arguments given");
                System.exit(1);
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
            return serving.getServingStatus();

        } catch (ParseException | SushiException e) {
            formatter.printHelp("options", options);
            System.exit(1);
        }
        return null;
    }
}
