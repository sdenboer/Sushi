package com.sushi.components.common.mappers;

import com.sushi.components.common.message.serving.SushiPullServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.*;


public class SushiPullServingMapper implements SushiMessageMapper<SushiPullServing> {
    @Override
    public SushiPullServing from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        String fileSize = wrappers.getOrDefault(CONTENT_LENGTH, null);
        return SushiPullServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .encryption(wrappers.getOrDefault(ENCRYPTION, null))
                .fileSize(fileSize == null ? null : Long.parseLong(fileSize))
                .content(wrappers.getOrDefault(CONTENT, null))
                .build();
    }
}
