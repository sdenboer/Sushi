package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.message.SushiMessageMapper;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.*;

public class SushiRemoveOrderMapper implements SushiMessageMapper<SushiRemoveOrder> {

    @Override
    public SushiRemoveOrder from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiRemoveOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .build();
    }
}
