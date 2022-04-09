package com.sushi.components.protocol.push;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.message.wrappers.WrapperField.*;

public class PushOrderMapper implements MessageMapper<PushOrder> {

    @Override
    public PushOrder from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);
        ContentType contentType = ContentType.fromString(
                MessageMapper.getStringWrapper(wrappers, CONTENT));
        Long contentLength = MessageMapper.getLongWrapper(wrappers, CONTENT_LENGTH);
        PayloadMetaData metaData = new PayloadMetaData(contentType, contentLength);

        return PushOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .payloadContext(new PayloadContext(metaData, null))
                .build();
    }
}
