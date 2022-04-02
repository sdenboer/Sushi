package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import com.sushi.components.common.message.wrappers.TextPayload;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class SushiFileServing extends SushiServing implements HasPayload<TextPayload> {

    private final String content;
    private final TextPayload payload;
    private final int payloadSize;

    @Builder
    public SushiFileServing(SushiServingStatus sushiServingStatus, UUID orderId, String content, int payloadSize, TextPayload payload) {
        super(sushiServingStatus, orderId);
        this.payload = payload;
        this.content = content;
        this.payloadSize = payloadSize;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        return Map.of(
                SushiWrapperField.CONTENT, content,
                SushiWrapperField.CONTENT_LENGTH, String.valueOf(payloadSize)
        );
    }

    @Override
    public TextPayload getPayload() {
        return payload;
    }
}
