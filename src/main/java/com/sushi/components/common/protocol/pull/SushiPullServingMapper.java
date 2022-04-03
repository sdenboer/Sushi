package com.sushi.components.common.protocol.pull;

import com.sushi.components.common.message.SushiMessageMapper;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.*;


public class SushiPullServingMapper implements SushiMessageMapper<SushiPullServing> {
    @Override
    public SushiPullServing from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiPullServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                //optional
                .encryption(SushiMessageMapper.getStringWrapper(wrappers, ENCRYPTION))
                .fileSize(SushiMessageMapper.getLongWrapper(wrappers, ENCRYPTION))
                .content(SushiMessageMapper.getStringWrapper(wrappers, CONTENT))
                .build();
    }
}
