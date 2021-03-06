package com.sushi.client.push;

import com.sushi.client.cmd.OrderOption;
import com.sushi.client.utils.Constants;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.Payload;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.protocol.push.PushOrder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sushi.client.cmd.CommandLineOptions.*;
import static com.sushi.components.utils.Constants.FILE_DIR;

public class PushOrderOption implements OrderOption {

    @Override
    public Options getMethodOptions() {
        Options options = new Options();
        addOptionToGroup(options, true, hostOption, portOption, localToRemoteFileOption);
        return options;
    }

    @Override
    public Order createOrder(CommandLine cmd) {
        String localToRemoteFile = getValueFromCMD(cmd, Constants.FILE);
        String[] split = localToRemoteFile.split(":");

        if (split.length != 2) {
            System.out.println("incorrect file syntax");
            System.exit(1);
        }

        String localFile = split[0];
        Path localPath = Paths.get(localFile);
        if (!Files.exists(localPath)) {
            localPath = Paths.get(FILE_DIR, localFile);
        }
        long size = 0L;

        try {
            size = Files.size(localPath);
        } catch (IOException e) {
            System.out.println("Cannot find file " + localFile);
            System.exit(1);
        }

        String remoteFile = split[1];
        Path remotePath = Paths.get(remoteFile);
        String fileName = localPath.getFileName().toString();
        String dir = remotePath.toString();
        PayloadMetaData metaData = new PayloadMetaData(ContentType.FILE, size);
        Payload payload = new Payload(localPath.toString());

        return PushOrder.builder()
                .host(getValueFromCMD(cmd, Constants.HOST))
                .port(getIntValueFromCMD(cmd, Constants.PORT))
                .orderId(UUID.randomUUID())
                .dir(dir)
                .fileName(fileName)
                .payloadContext(new PayloadContext(metaData, payload))
                .build();
    }
}
