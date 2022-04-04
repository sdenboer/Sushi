package com.sushi.components.common.protocol.status;

import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.ContentType;
import com.sushi.components.common.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.WrapperField.*;

public class StatusServingMapper implements MessageMapper<StatusServing> {

    @Override
    public StatusServing from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return StatusServing.builder()
                .servingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                //optional
                .contentType(ContentType.fromString(MessageMapper.getStringWrapper(wrappers, CONTENT)))
                .payloadSize(MessageMapper.getIntegerWrapper(wrappers, CONTENT_LENGTH))
                .build();
    }
}
