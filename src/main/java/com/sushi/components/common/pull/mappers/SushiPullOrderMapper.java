package com.sushi.components.common.pull.mappers;

import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderMapper;
import com.sushi.components.common.order.SushiOrderWrapperField;
import com.sushi.components.common.pull.SushiPullOrder;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.order.SushiOrderWrapperField.*;

public class SushiPullOrderMapper implements SushiOrderMapper<SushiPullOrder> {
    @Override
    public SushiPullOrder from(String request) {
        Map<SushiOrderWrapperField, String> wrappers = SushiOrder.mapToHeaders(request);

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
