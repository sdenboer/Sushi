package com.sushi.components.common.protocol.file;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.TextPayload;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class FileServing extends Serving implements HasPayload<TextPayload> {

    private final String content;
    private final TextPayload payload;
    private final Integer payloadSize;

    @Builder
    public FileServing(ServingStatus servingStatus, UUID orderId, String content, Integer payloadSize, TextPayload payload) {
        super(servingStatus, orderId);
        this.payload = payload;
        this.content = content;
        this.payloadSize = payloadSize;
    }

    @Override
    public Map<WrapperField, String> optionalWrappers() {
        EnumMap<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.CONTENT, content);
        wrappers.put(WrapperField.CONTENT_LENGTH, String.valueOf(payloadSize));
        return wrappers;
    }

    @Override
    public TextPayload getPayload() {
        return payload;
    }
}
