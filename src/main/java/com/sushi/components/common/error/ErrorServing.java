package com.sushi.components.common.error;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapper;
import lombok.Builder;
import lombok.NonNull;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class ErrorServing extends SushiServing {

    @Builder
    public ErrorServing(SushiServingStatus sushiServingStatus, UUID orderId) {
        super(sushiServingStatus, orderId, null);
    }

    @Override
    public @NonNull Set<SushiServingWrapper> optionalSushiWrappers() {
        return Collections.emptySet();
    }

}
