package com.sushi.components.common.file.mappers;

import com.sushi.components.common.file.SushiFileOrder;
import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderWrapperField;
import com.sushi.components.common.order.SushiOrderMapper;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.order.SushiOrderWrapperField.*;

public class SushiFileOrderMapper implements SushiOrderMapper<SushiFileOrder> {

    @Override
    public SushiFileOrder from(String request) {
        Map<SushiOrderWrapperField, String> wrappers = SushiOrder.mapToHeaders(request);

        return SushiFileOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .build();
    }
}
