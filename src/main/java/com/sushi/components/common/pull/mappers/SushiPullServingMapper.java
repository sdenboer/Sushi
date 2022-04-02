package com.sushi.components.common.pull.mappers;

import com.sushi.components.common.order.SushiServingMapper;
import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.serving.SushiServingWrapperField.*;

public class SushiPullServingMapper implements SushiServingMapper<SushiPullServing> {
    @Override
    public SushiPullServing from(String request) {
        Map<SushiServingWrapperField, String> wrappers = SushiServing.mapToHeaders(request);

        String fileSize = wrappers.getOrDefault(FILE_SIZE, null);
        return SushiPullServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .encryption(wrappers.getOrDefault(ENCRYPTION, null))
                .fileSize(fileSize == null ? null : Long.parseLong(fileSize))
                .content(wrappers.getOrDefault(CONTENT, null))
                .build();
    }
}
