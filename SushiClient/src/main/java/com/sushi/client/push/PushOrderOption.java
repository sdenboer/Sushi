package com.sushi.client.push;

import com.sushi.client.Constants;
import com.sushi.client.OrderOption;
import com.sushi.components.error.exceptions.CheckedSushiException;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.FilePayload;
import com.sushi.components.protocol.push.PushOrder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sushi.client.CommandLineOptions.*;

public class PushOrderOption implements OrderOption {

    @Override
    public Options getMethodOptions() {
        Options options = new Options();
        addOptionToGroup(options, true, hostOption, portOption, localToRemoteFileOption);
        return options;
    }

    @Override
    public Order createOrder(CommandLine cmd) throws CheckedSushiException {
        String localToRemoteFile = getValueFromCMD(cmd, Constants.FILE);
        String[] split = localToRemoteFile.split(":");

        if (split.length != 2) {
            throw new CheckedSushiException("incorrect file syntax");
        }

        String localFile = split[0];
        Path localPath = Paths.get(localFile);
        long size;

        try {
            size = Files.size(localPath);
        } catch (IOException e) {
            throw new CheckedSushiException("cannot find file");
        }

        String remoteFile = split[1];
        Path remotePath = Paths.get(remoteFile);
        String fileName = localPath.getFileName().toString();
        String dir = remotePath.toString();

        return PushOrder.builder()
                .host(getValueFromCMD(cmd, Constants.HOST))
                .port(getIntValueFromCMD(cmd, Constants.PORT))
                .orderId(UUID.randomUUID())
                .contentType(ContentType.FILE)
                .dir(dir)
                .fileName(fileName)
                .fileSize(size)
                .payload(new FilePayload(localPath))
                .build();
    }
}
