package com.sushi.components.common.protocol.push;

import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.WrapperField.ORDER_ID;
import static com.sushi.components.common.message.wrappers.WrapperField.STATUS;

public class PushServingMapper implements MessageMapper<PushServing> {
    @Override
    public PushServing from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return PushServing.builder()
                .sushiServingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }
}
