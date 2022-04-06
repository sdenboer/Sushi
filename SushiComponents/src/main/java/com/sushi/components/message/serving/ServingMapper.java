package com.sushi.components.message.serving;

import static com.sushi.components.message.wrappers.WrapperField.CONTENT;
import static com.sushi.components.message.wrappers.WrapperField.CONTENT_LENGTH;
import static com.sushi.components.message.wrappers.WrapperField.ORDER_ID;
import static com.sushi.components.message.wrappers.WrapperField.STATUS;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.serving.Serving.ServingBuilder;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.Map;
import java.util.UUID;

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
