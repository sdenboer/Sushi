package com.sushi.components.protocol.push;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.message.wrappers.WrapperField.ORDER_ID;
import static com.sushi.components.message.wrappers.WrapperField.STATUS;

public class PushServingMapper implements MessageMapper<PushServing> {
    @Override
    public PushServing from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return PushServing.builder()
                .servingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }
}
