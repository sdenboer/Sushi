package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.TextPayload;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

public class SushiFileServing extends SushiServing implements HasPayload<TextPayload> {

    private final String content;
    private final TextPayload payload;

    @Builder
    public SushiFileServing(SushiServingStatus sushiServingStatus, UUID orderId, String content, TextPayload payload) {
        super(sushiServingStatus, orderId);
        this.payload = payload;
        this.content = content;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        return Map.of(
                SushiWrapperField.CONTENT, content
        );
    }

    @Override
    public TextPayload getPayload() {
        return payload;
    }
}
