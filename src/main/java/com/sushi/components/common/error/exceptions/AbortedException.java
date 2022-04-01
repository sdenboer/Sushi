package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class AbortedException extends SushiException {

    public AbortedException(UUID orderId) {
        super(SushiServingStatus.ABORTED, orderId);
    }
}
