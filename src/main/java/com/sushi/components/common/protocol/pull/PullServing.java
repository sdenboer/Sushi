package com.sushi.components.common.protocol.pull;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
public class PullServing extends Serving implements HasPayload<FilePayload> {

    private final String encryption;
    private final String content;
    private final Long fileSize;
    private final FilePayload payload;

    @Builder
    public PullServing(ServingStatus servingStatus, UUID orderId, String encryption, String content, Long fileSize, FilePayload payload) {
        super(servingStatus, orderId);
        this.payload = payload;
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
    }

    @Override
    public Map<WrapperField, String> optionalWrappers() {
        EnumMap<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.CONTENT, content);
        wrappers.put(WrapperField.ENCRYPTION, encryption);
        wrappers.put(WrapperField.CONTENT_LENGTH, Objects.toString(fileSize, null));
        return wrappers;
    }

    @Override
    public FilePayload getPayload() {
        return this.payload;
    }
}
