package com.sushi.components.common.protocol.push;

import com.sushi.components.common.message.SushiMessageMapper;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.*;

public class SushiPushOrderMapper implements SushiMessageMapper<SushiPushOrder> {

    @Override
    public SushiPushOrder from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiPushOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .encryption(wrappers.get(ENCRYPTION))
                .content(wrappers.get(CONTENT))
                .fileSize((Long.parseLong(wrappers.get(CONTENT_LENGTH))))
                .build();
    }
}
