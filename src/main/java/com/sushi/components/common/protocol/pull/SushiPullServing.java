package com.sushi.components.common.protocol.pull;

import com.sushi.components.common.message.serving.SushiServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
public class SushiPullServing extends SushiServing implements HasPayload<FilePayload> {

    private final String encryption;
    private final String content;
    private final Long fileSize;
    private final FilePayload payload;

    @Builder
    public SushiPullServing(SushiServingStatus sushiServingStatus, UUID orderId, String encryption, String content, Long fileSize, FilePayload payload) {
        super(sushiServingStatus, orderId);
        this.payload = payload;
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.CONTENT, content);
        wrappers.put(SushiWrapperField.ENCRYPTION, encryption);
        wrappers.put(SushiWrapperField.CONTENT_LENGTH, Objects.toString(fileSize, null));
        return wrappers;
    }

    @Override
    public FilePayload getPayload() {
        return this.payload;
    }
}
