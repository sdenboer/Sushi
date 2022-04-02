package com.sushi.components.common.push.mappers;

import com.sushi.components.common.order.SushiServingMapper;
import com.sushi.components.common.push.SushiPushServing;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapperField;

import java.util.Map;

public class SushiPushServingMapper implements SushiServingMapper<SushiPushServing> {
    @Override
    public SushiPushServing from(String request) {
        Map<SushiServingWrapperField, String> wrappers = SushiServing.mapToHeaders(request);

        return SushiPushServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(SushiServingWrapperField.STATUS)))
                .build();
    }
}
