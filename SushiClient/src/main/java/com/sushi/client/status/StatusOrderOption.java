package com.sushi.client.status;

import static com.sushi.client.cmd.CommandLineOptions.hostOption;
import static com.sushi.client.cmd.CommandLineOptions.portOption;
import static com.sushi.client.utils.Constants.HOST;
import static com.sushi.client.utils.Constants.PORT;

import com.sushi.client.cmd.OrderOption;
import com.sushi.components.message.order.Order;
import com.sushi.components.protocol.status.StatusOrder;
import java.util.UUID;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class StatusOrderOption implements OrderOption {

    @Override
    public Options getMethodOptions() {
        Options options = new Options();
        addOptionToGroup(options, true, hostOption, portOption);
        return options;
    }

    @Override
    public Order createOrder(CommandLine cmd) {
        return StatusOrder.builder()
            .host(getValueFromCMD(cmd, HOST))
            .port(getIntValueFromCMD(cmd, PORT))
            .orderId(UUID.randomUUID())
            .build();
    }

}
