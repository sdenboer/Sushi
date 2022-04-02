package com.sushi.components.common.file.mappers;

import com.sushi.components.common.file.SushiFileServing;
import com.sushi.components.common.order.SushiServingMapper;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.serving.SushiServingWrapperField.*;

public class SushiFileServingMapper implements SushiServingMapper<SushiFileServing> {

    @Override
    public SushiFileServing from(String request) {
        Map<SushiServingWrapperField, String> wrappers = SushiServing.mapToHeaders(request);

        return SushiFileServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .content(wrappers.getOrDefault(CONTENT, null))
                .build();
    }
}
