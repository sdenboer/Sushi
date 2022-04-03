package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class SushiPushServing extends SushiServing {

    private final String content;

    @Builder
    public SushiPushServing(SushiServingStatus sushiServingStatus, UUID orderId, String content) {
        super(sushiServingStatus, orderId);
        this.content = content;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.CONTENT, content);
        return wrappers;
    }

}
