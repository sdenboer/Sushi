package com.sushi.client.file;

import static com.sushi.client.cmd.CommandLineOptions.fileOption;
import static com.sushi.client.cmd.CommandLineOptions.hostOption;
import static com.sushi.client.cmd.CommandLineOptions.portOption;
import static com.sushi.client.utils.Constants.FILE;
import static com.sushi.client.utils.Constants.HOST;
import static com.sushi.client.utils.Constants.PORT;

import com.sushi.client.cmd.OrderOption;
import com.sushi.components.message.order.Order;
import com.sushi.components.protocol.file.FileOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

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
