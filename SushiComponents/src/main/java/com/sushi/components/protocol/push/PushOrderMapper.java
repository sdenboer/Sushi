package com.sushi.components.protocol.push;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.message.wrappers.WrapperField.*;

public class PushOrderMapper implements MessageMapper<PushOrder> {

    @Override
    public PushOrder from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return PushOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .encryption(wrappers.get(ENCRYPTION))
                .contentType(ContentType.fromString(wrappers.get(CONTENT)))
                .fileSize((Long.parseLong(wrappers.get(CONTENT_LENGTH))))
                .build();
    }
}
