package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.message.serving.SushiServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class SushiRemoveServing extends SushiServing {

    @Builder
    public SushiRemoveServing(SushiServingStatus sushiServingStatus, UUID orderId) {
        super(sushiServingStatus, orderId);
    }

    @Override
    public @NonNull Map<SushiWrapperField, String> optionalSushiWrappers() {
        return Collections.emptyMap();
    }
}
