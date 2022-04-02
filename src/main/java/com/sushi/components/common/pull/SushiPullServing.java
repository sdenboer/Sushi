package com.sushi.components.common.pull;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapper;
import com.sushi.components.common.serving.SushiServingWrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public class SushiPullServing extends SushiServing {

    private final String encryption;
    private final String content;
    private final Long fileSize;

    @Builder
    public SushiPullServing(SushiServingStatus sushiServingStatus, UUID orderId, String encryption, String content, Long fileSize, SushiPullServingPayload payload) {
        super(sushiServingStatus, orderId, payload);
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
    }

    @Override
    public Set<SushiServingWrapper> optionalSushiWrappers() {
        return Set.of(
                new SushiServingWrapper(SushiServingWrapperField.ENCRYPTION, encryption),
                new SushiServingWrapper(SushiServingWrapperField.CONTENT, content),
                new SushiServingWrapper(SushiServingWrapperField.FILE_SIZE, Objects.toString(fileSize, null))
        );
    }

}
