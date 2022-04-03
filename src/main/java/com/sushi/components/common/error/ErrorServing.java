package com.sushi.components.common.error;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class ErrorServing extends Serving {

    @Builder
    public ErrorServing(ServingStatus servingStatus, UUID orderId) {
        super(servingStatus, orderId);
    }

    public Map<WrapperField, String> optionalWrappers() {
        return Collections.emptyMap();
    }

}
