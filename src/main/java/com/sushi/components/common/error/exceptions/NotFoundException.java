package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class NotFoundException extends SushiException {
    public NotFoundException(UUID orderId) {
        super(SushiServingStatus.NOT_FOUND, orderId);
    }
}
