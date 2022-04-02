package com.sushi.components.common.push.mappers;

import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderMapper;
import com.sushi.components.common.order.SushiOrderWrapperField;
import com.sushi.components.common.push.SushiPushOrder;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.order.SushiOrderWrapperField.*;

public class SushiPushOrderMapper implements SushiOrderMapper<SushiPushOrder> {

    @Override
    public SushiPushOrder from(String request) {
        Map<SushiOrderWrapperField, String> wrappers = SushiOrder.mapToHeaders(request);

        return SushiPushOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .encryption(wrappers.get(ENCRYPTION))
                .content(wrappers.get(CONTENT))
                .fileSize((Long.parseLong(wrappers.get(FILE_SIZE))))
                .build();
    }
}
