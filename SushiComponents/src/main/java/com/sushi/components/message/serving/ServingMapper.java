package com.sushi.components.message.serving;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.serving.Serving.ServingBuilder;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.message.wrappers.WrapperField.*;

public class ServingMapper implements MessageMapper<Serving> {

    @Override
    public Serving from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        ServingBuilder servingBuilder = Serving.builder()
                .servingStatus(ServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)));

        if (wrappers.containsKey(CONTENT)) {
            ContentType contentType = ContentType.fromString(
                    MessageMapper.getStringWrapper(wrappers, CONTENT));
            Long contentLength = MessageMapper.getLongWrapper(wrappers, CONTENT_LENGTH);
            PayloadMetaData metaData = new PayloadMetaData(contentType, contentLength);
            servingBuilder.payloadContext(new PayloadContext(metaData, null));
        }
        return servingBuilder.build();
    }
}
