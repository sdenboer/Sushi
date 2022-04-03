package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.ServingStatus;

import java.util.UUID;

public class AbortedException extends SushiException {

    public AbortedException(UUID orderId) {
        super(ServingStatus.ABORTED, orderId);
    }
}
