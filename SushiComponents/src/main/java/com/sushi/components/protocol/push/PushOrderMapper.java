package com.sushi.components.protocol.push;

import static com.sushi.components.message.wrappers.WrapperField.CONTENT;
import static com.sushi.components.message.wrappers.WrapperField.CONTENT_LENGTH;
import static com.sushi.components.message.wrappers.WrapperField.DIR;
import static com.sushi.components.message.wrappers.WrapperField.FILE;
import static com.sushi.components.message.wrappers.WrapperField.HOST;
import static com.sushi.components.message.wrappers.WrapperField.ORDER_ID;
import static com.sushi.components.message.wrappers.WrapperField.PORT;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.Map;
import java.util.UUID;

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
