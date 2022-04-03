package com.sushi.components.common.protocol.pull;

import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.WrapperField.*;


public class PullServingMapper implements MessageMapper<PullServing> {
    @Override
    public PullServing from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return PullServing.builder()
                .servingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                //optional
                .encryption(MessageMapper.getStringWrapper(wrappers, ENCRYPTION))
                .fileSize(MessageMapper.getLongWrapper(wrappers, ENCRYPTION))
                .content(MessageMapper.getStringWrapper(wrappers, CONTENT))
                .build();
    }
}
