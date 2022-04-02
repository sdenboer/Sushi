package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class ErrorServing extends SushiServing {

    @Builder
    public ErrorServing(SushiServingStatus sushiServingStatus, UUID orderId) {
        super(sushiServingStatus, orderId);
    }

    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        return Collections.emptyMap();
    }

}
