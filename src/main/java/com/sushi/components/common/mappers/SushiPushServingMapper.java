package com.sushi.components.common.mappers;

import com.sushi.components.common.message.serving.SushiPushServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.ORDER_ID;
import static com.sushi.components.common.message.wrappers.SushiWrapperField.STATUS;

public class SushiPushServingMapper implements SushiMessageMapper<SushiPushServing> {
    @Override
    public SushiPushServing from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiPushServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }
}
