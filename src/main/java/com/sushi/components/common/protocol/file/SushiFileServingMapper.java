package com.sushi.components.common.protocol.file;

import com.sushi.components.common.mappers.SushiMessageMapper;
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
                //optional
                .content(SushiMessageMapper.getStringWrapper(wrappers, CONTENT))
                .payloadSize(SushiMessageMapper.getIntegerWrapper(wrappers, CONTENT_LENGTH))
                .build();
    }
}
