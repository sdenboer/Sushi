package com.sushi.components.error.exceptions;

import com.sushi.components.message.serving.ServingStatus;

import java.util.UUID;

public class AbortedException extends SushiException {

    public AbortedException(UUID orderId) {
        super(ServingStatus.ABORTED, orderId);
    }
}
