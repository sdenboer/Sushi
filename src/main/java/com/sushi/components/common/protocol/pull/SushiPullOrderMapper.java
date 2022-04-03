package com.sushi.components.common.protocol.pull;

import com.sushi.components.common.message.SushiMessageMapper;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.*;

public class SushiPullOrderMapper implements SushiMessageMapper<SushiPullOrder> {
    @Override
    public SushiPullOrder from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiPullOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .encryption(wrappers.get(ENCRYPTION))
                .build();
    }
}
