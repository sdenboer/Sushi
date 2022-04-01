package com.sushi.components.server.pull;

import com.sushi.components.common.error.ErrorServing;
import com.sushi.components.common.error.ServerErrorException;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.server.FileServingService;
import com.sushi.components.server.OrderService;
import com.sushi.components.server.TextServingResponse;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PullOrderService extends OrderService<SushiPullOrder> {

    public PullOrderService(AsynchronousSocketChannel channel) {
        super(channel);
    }

    @Override
    public void handle(SushiPullOrder sushiOrder) {
        UUID orderId = sushiOrder.getOrderId();
        try {
            Path path = Paths.get(sushiOrder.getDir(), sushiOrder.getFileName());
            long size = Files.size(path);
            SushiPullServing serving = new SushiPullServing(SushiServingStatus.OK, orderId, "aes", "file", size);
            new FileServingService(channel).sendServing(serving, path);
        } catch (IOException e) {
            ErrorServing errorServing = new ErrorServing(SushiServingStatus.NOT_FOUND, orderId);
            new TextServingResponse(channel).sendServing(errorServing);
        }
    }

}
