package com.sushi.components.common.protocol.file;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.ContentType;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.TextPayload;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FileServing extends Serving implements HasPayload<TextPayload> {

    private final ContentType contentType;
    private final TextPayload payload;
    private final Integer payloadSize;

    @Builder
    public FileServing(ServingStatus servingStatus, UUID orderId, ContentType contentType, Integer payloadSize, TextPayload payload) {
        super(servingStatus, orderId);
        this.payload = payload;
        this.contentType = contentType;
        this.payloadSize = payloadSize;
    }

    @Override
    public void addOptionalWrappers() {
        addWrapper(WrapperField.CONTENT, contentType.getType());
        addWrapper(WrapperField.CONTENT_LENGTH, String.valueOf(payloadSize));
    }

    @Override
    public TextPayload getPayload() {
        return payload;
    }
}
