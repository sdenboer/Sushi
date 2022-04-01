package com.sushi.components.common.error;

import com.sushi.components.common.pull.SushiPullServing;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapper;
import com.sushi.components.common.serving.SushiServingWrapperField;
import lombok.Builder;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.sushi.components.common.serving.SushiServingWrapperField.ORDER_ID;
import static com.sushi.components.common.serving.SushiServingWrapperField.STATUS;

public class ErrorServing extends SushiServing {

    @Builder
    public ErrorServing(SushiServingStatus sushiServingStatus, UUID orderId) {
        super(sushiServingStatus, orderId);
    }

    @Override
    public @NonNull Set<SushiServingWrapper> optionalSushiWrappers() {
        return Collections.emptySet();
    }

    public static SushiPullServing fromRequest(String request) {
        Map<SushiServingWrapperField, String> wrappers = SushiServing.mapToHeaders(request);

        return SushiPullServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }

}
