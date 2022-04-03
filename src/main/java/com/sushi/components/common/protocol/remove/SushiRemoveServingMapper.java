package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.mappers.SushiMessageMapper;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.ORDER_ID;
import static com.sushi.components.common.message.wrappers.SushiWrapperField.STATUS;

public class SushiRemoveServingMapper implements SushiMessageMapper<SushiRemoveServing> {

    @Override
    public SushiRemoveServing from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiRemoveServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }
}
