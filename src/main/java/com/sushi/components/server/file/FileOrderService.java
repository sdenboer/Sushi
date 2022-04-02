package com.sushi.components.server.file;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.message.order.SushiFileOrder;
import com.sushi.components.common.message.serving.SushiFileServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.TextPayload;
import com.sushi.components.common.senders.SushiMessageSender;
import com.sushi.components.server.OrderService;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOrderService implements OrderService<SushiFileOrder> {

    @Override
    public void handle(AsynchronousSocketChannel socketChannel, SushiFileOrder sushiOrder, OrderContext orderContext) {

        Map<String, String> files = new HashMap<>();
        try (Stream<Path> stream = Files.list(Paths.get(sushiOrder.getDir()))) {
            stream.filter(file -> !Files.isDirectory(file))
                    .filter(f -> f.getFileName().toString().equals(sushiOrder.getFileName()))
                    .forEach(path -> {
                        try (InputStream is = Files.newInputStream(path)) {
                            String bytes = DigestUtils.sha256Hex(is);
                            System.out.println(path + ": " + bytes);
                            files.put(path.toString(), bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        String payload = serializePayload(files);
        int payloadSize = payload.getBytes(StandardCharsets.UTF_8).length;
        TextPayload textPayload = new TextPayload(payload);
        SushiFileServing serving = new SushiFileServing(SushiServingStatus.OK, sushiOrder.getOrderId(), "txt", payloadSize, textPayload);
        new SushiMessageSender().send(socketChannel, serving);
    }

    private String serializePayload(Map<String, String> files) {
        return files.entrySet()
                .stream()
                .map(s -> s.getKey() + ": " + s.getValue())
                .collect(Collectors.joining("\n"));

    }

}
