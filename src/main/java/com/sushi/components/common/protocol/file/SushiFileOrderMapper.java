package com.sushi.components.common.protocol.file;

import com.sushi.components.common.message.SushiMessageMapper;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.*;

public class SushiFileOrderMapper implements SushiMessageMapper<SushiFileOrder> {

    @Override
    public SushiFileOrder from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiFileOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .build();
    }
}
