package com.sushi.components.message;

import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.Map;

public interface Message {

    Map<WrapperField, String> getWrappers();

    PayloadContext getPayloadContext();

    default boolean hasPayload() {
        return getPayloadContext() != null;
    }

    default String toRequest() {
        return """
            %s
                       
            """.formatted(MessageMapper.serialize(getWrappers()));
    }


}
