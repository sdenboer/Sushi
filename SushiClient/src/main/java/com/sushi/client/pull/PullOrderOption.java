package com.sushi.client.pull;

import com.sushi.client.OrderOption;
import com.sushi.components.message.order.Order;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class PullOrderOption implements OrderOption {
    @Override
    public Options getMethodOptions() {
        return null;
    }

    @Override
    public Order createOrder(CommandLine cmd) {
        return null;
    }
}
