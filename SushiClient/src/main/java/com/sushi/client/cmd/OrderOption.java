package com.sushi.client.cmd;

import com.sushi.components.message.order.Order;
import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public interface OrderOption {

    Options getMethodOptions();

    Order createOrder(CommandLine cmd);

    default void addOptionToGroup(Options optionGroup, boolean necessity, Option... options) {
        Arrays.stream(options).forEach(option -> {
            option.setRequired(necessity);
            optionGroup.addOption(option);
        });
    }

    default String getValueFromCMD(CommandLine cmd, String field) {
        return cmd.getOptionValue(field, null).trim();
    }

    default Integer getIntValueFromCMD(CommandLine cmd, String field) {
        String value = getValueFromCMD(cmd, field);
        return value == null ? null : Integer.parseInt(value);
    }

}
