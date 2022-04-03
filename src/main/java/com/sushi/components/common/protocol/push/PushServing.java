package com.sushi.components.common.protocol.push;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PushServing extends Serving {

    private final String content;

    @Builder
    public PushServing(ServingStatus servingStatus, UUID orderId, String content) {
        super(servingStatus, orderId);
        this.content = content;
    }

    @Override
    public Map<WrapperField, String> optionalSushiWrappers() {
        EnumMap<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.CONTENT, content);
        return wrappers;
    }

}
