package com.sushi.client;

import com.sushi.client.file.FileOrderOption;
import com.sushi.client.pull.PullOrderOption;
import com.sushi.client.push.PushOrderOption;
import com.sushi.client.remove.RemoveOrderOption;
import com.sushi.client.status.StatusOrderOption;
import com.sushi.components.error.exceptions.CheckedSushiException;
import org.apache.commons.cli.CommandLine;

import static com.sushi.client.Constants.*;

public class OrderOptionFactory {

    private final CommandLine cmd;

    public OrderOptionFactory(CommandLine cmd) {
        this.cmd = cmd;
    }

    public OrderOption getOrderOption() throws CheckedSushiException {

        if (hasOption(VERIFY_METHOD)) {
            return new StatusOrderOption();
        }
        if (hasOption(LIST_METHOD)) {
            return new FileOrderOption();
        }
        if (hasOption(BACKUP_METHOD)) {
            return new PushOrderOption();
        }
        if (hasOption(FETCH_METHOD)) {
            return new PullOrderOption();
        }
        if (hasOption(REMOVE_METHOD)) {
            return new RemoveOrderOption();
        }
        throw new CheckedSushiException("Not implemented");
    }

    private boolean hasOption(String method) {
        return cmd.hasOption(method);
    }

}
