package com.sushi.components.common.file;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapper;
import com.sushi.components.common.serving.SushiServingWrapperField;
import lombok.Builder;
import lombok.NonNull;

import java.util.Set;
import java.util.UUID;

public class SushiFileServing extends SushiServing {

    private final String content;

    @Builder
    public SushiFileServing(SushiServingStatus sushiServingStatus, UUID orderId, String content, SushiFileServingPayload payload) {
        super(sushiServingStatus, orderId, payload);
        this.content = content;
    }

    @Override
    public @NonNull Set<SushiServingWrapper> optionalSushiWrappers() {
        return Set.of(
                new SushiServingWrapper(SushiServingWrapperField.CONTENT, content)
        );
    }
}
