package com.sushi.client.cmd;

import static com.sushi.client.utils.Constants.BACKUP_METHOD;
import static com.sushi.client.utils.Constants.FETCH_METHOD;
import static com.sushi.client.utils.Constants.LIST_METHOD;
import static com.sushi.client.utils.Constants.REMOVE_METHOD;
import static com.sushi.client.utils.Constants.VERIFY_METHOD;

import com.sushi.client.exceptions.SushiException;
import com.sushi.client.file.FileOrderOption;
import com.sushi.client.pull.PullOrderOption;
import com.sushi.client.push.PushOrderOption;
import com.sushi.client.remove.RemoveOrderOption;
import com.sushi.client.status.StatusOrderOption;
import org.apache.commons.cli.CommandLine;

public class OrderOptionFactory {

    private final CommandLine cmd;

    public OrderOptionFactory(CommandLine cmd) {
        this.cmd = cmd;
    }

    public OrderOption getOrderOption() throws SushiException {

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
        throw new SushiException();
    }

    private boolean hasOption(String method) {
        return cmd.hasOption(method);
    }

}
