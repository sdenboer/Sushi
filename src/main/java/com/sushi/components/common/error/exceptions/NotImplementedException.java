package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class NotImplementedException extends SushiException {
    public NotImplementedException(UUID orderId) {
        super(SushiServingStatus.NOT_IMPLEMENTED, orderId);
    }
}
