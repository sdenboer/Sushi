package com.sushi.components.senders;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.Payload;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.utils.OrderContext;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServingSender {

    public static void send(AsynchronousByteChannel socketChannel, String message,
        OrderContext orderContext) {
        int payloadSize = message.getBytes(StandardCharsets.UTF_8).length;
        Payload payload = new Payload(message);
        PayloadMetaData metaData = new PayloadMetaData(ContentType.TXT, payloadSize);
        Serving serving = new Serving(ServingStatus.OK, orderContext.orderId(),
            new PayloadContext(metaData, payload));
        MessageSender.send(socketChannel, serving);
    }

}
