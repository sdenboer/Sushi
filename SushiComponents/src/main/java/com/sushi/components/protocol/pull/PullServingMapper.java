package com.sushi.components.protocol.pull;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.message.wrappers.WrapperField.*;


public class PullServingMapper implements MessageMapper<PullServing> {
    @Override
    public PullServing from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return PullServing.builder()
                .servingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                //optional
                .encryption(MessageMapper.getStringWrapper(wrappers, ENCRYPTION))
                .fileSize(MessageMapper.getLongWrapper(wrappers, CONTENT_LENGTH))
                .contentType(ContentType.fromString(MessageMapper.getStringWrapper(wrappers, CONTENT)))
                .build();
    }
}
