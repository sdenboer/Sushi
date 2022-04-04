package com.sushi.components.protocol.pull;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.FilePayload;
import com.sushi.components.message.wrappers.HasPayload;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class PullServing extends Serving implements HasPayload<FilePayload> {

    private final String encryption;
    private final ContentType contentType;
    private final Long fileSize;
    private final FilePayload payload;

    @Builder
    public PullServing(ServingStatus servingStatus, UUID orderId, String encryption, ContentType contentType, Long fileSize, FilePayload payload) {
        super(servingStatus, orderId);
        this.payload = payload;
        this.encryption = encryption;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    @Override
    public void addOptionalWrappers() {
        addWrapper(WrapperField.CONTENT, contentType.getType());
        addWrapper(WrapperField.ENCRYPTION, encryption);
        addWrapper(WrapperField.CONTENT_LENGTH, Objects.toString(fileSize, null));
    }

    @Override
    public FilePayload getPayload() {
        return this.payload;
    }
}
