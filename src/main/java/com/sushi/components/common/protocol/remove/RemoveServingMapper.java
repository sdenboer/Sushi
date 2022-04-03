package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.WrapperField.ORDER_ID;
import static com.sushi.components.common.message.wrappers.WrapperField.STATUS;

public class RemoveServingMapper implements MessageMapper<RemoveServing> {

    @Override
    public RemoveServing from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return RemoveServing.builder()
                .sushiServingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }
}
