package com.sushi.client.file;

import com.sushi.client.OrderOption;
import com.sushi.components.message.order.Order;
import com.sushi.components.protocol.file.FileOrder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sushi.client.CommandLineOptions.*;
import static com.sushi.client.Constants.*;

public class FileOrderOption implements OrderOption {

    @Override
    public Options getMethodOptions() {
        Options options = new Options();
        addOptionToGroup(options, true, fileOption, hostOption, portOption);
        return options;
    }

    @Override
    public Order createOrder(CommandLine cmd) {
        String file = getValueFromCMD(cmd, FILE);

        Path remotePath = Paths.get(file);
        String fileName = remotePath.getFileName().toString();
        String directory = remotePath.getParent().toString();

        return FileOrder.builder()
                .host(getValueFromCMD(cmd, HOST))
                .port(getIntValueFromCMD(cmd, PORT))
                .orderId(UUID.randomUUID())
                .dir(directory)
                .fileName(fileName)
                .build();
    }
}
