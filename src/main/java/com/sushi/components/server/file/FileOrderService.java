package com.sushi.components.server.file;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.file.SushiFileOrder;
import com.sushi.components.common.file.SushiFileServing;
import com.sushi.components.common.file.SushiFileServingPayload;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.server.OrderService;
import com.sushi.components.server.ServingService;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileOrderService implements OrderService<SushiFileOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, SushiFileOrder sushiOrder, OrderContext orderContext) {

        SushiFileServingPayload payload = new SushiFileServingPayload();
        try (Stream<Path> stream = Files.list(Paths.get(sushiOrder.getDir()))) {
            stream.filter(file -> !Files.isDirectory(file))
                    .filter(f -> f.getFileName().toString().equals(sushiOrder.getFileName()))
                    .forEach(path -> {
                        System.out.println("HELLO");
                        try (InputStream is = Files.newInputStream(path)) {
                            String bytes = DigestUtils.sha256Hex(is);
                            System.out.println(path + ": " + bytes);
                            payload.addFile(path.toString(), bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        SushiFileServing serving = new SushiFileServing(SushiServingStatus.OK, sushiOrder.getOrderId(), "txt", payload);
        ServingService servingService = new ServingService(socketChannel, serving);
        servingService.send();
    }

}
