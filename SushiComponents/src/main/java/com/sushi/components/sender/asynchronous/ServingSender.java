package com.sushi.components.sender.asynchronous;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.Payload;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.utils.OrderContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.nio.channels.AsynchronousByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServingSender {

    public static void sendTextPayload(AsynchronousByteChannel socketChannel, String message,
                                       OrderContext orderContext) {
        int payloadSize = message.getBytes(StandardCharsets.UTF_8).length;
        Payload payload = new Payload(message);
        PayloadMetaData metaData = new PayloadMetaData(ContentType.TXT, payloadSize);
        Serving serving = new Serving(ServingStatus.OK, orderContext.orderId(),
                new PayloadContext(metaData, payload));
        new AsyncChannelMessageSender().send(socketChannel, serving);
    }

    public static void sendFilePayload(AsynchronousByteChannel socketChannel, Path path,
                                       long size,
                                       OrderContext orderContext) {
        Payload payload = new Payload(path.toString());
        PayloadMetaData payloadMetaData = new PayloadMetaData(ContentType.FILE, size);
        Serving serving = new Serving(ServingStatus.OK, orderContext.orderId(),
                new PayloadContext(payloadMetaData, payload));
        new AsyncChannelMessageSender().send(socketChannel, serving);
    }

    public static void send(AsynchronousByteChannel socketChannel, ServingStatus status,
                            OrderContext orderContext) {
        Serving serving = new Serving(status, orderContext.orderId(), null);
        new AsyncChannelMessageSender().send(socketChannel, serving);
    }

    public static void send(AsynchronousByteChannel socketChannel,
                            OrderContext orderContext) {
        send(socketChannel, ServingStatus.OK, orderContext);
    }

}
