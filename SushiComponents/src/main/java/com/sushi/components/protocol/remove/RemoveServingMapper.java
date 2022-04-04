package com.sushi.components.protocol.remove;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.message.wrappers.WrapperField.ORDER_ID;
import static com.sushi.components.message.wrappers.WrapperField.STATUS;

public class RemoveServingMapper implements MessageMapper<RemoveServing> {

    @Override
    public RemoveServing from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return RemoveServing.builder()
                .servingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }
}
