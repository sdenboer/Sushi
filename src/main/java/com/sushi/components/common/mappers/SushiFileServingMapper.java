package com.sushi.components.common.mappers;

import com.sushi.components.common.message.serving.SushiFileServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.*;

public class SushiFileServingMapper implements SushiMessageMapper<SushiFileServing> {

    @Override
    public SushiFileServing from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiFileServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .content(wrappers.get(CONTENT))
                .payloadSize(Integer.parseInt(wrappers.get(CONTENT_LENGTH)))
                .build();
    }
}
