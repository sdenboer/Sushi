package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class InvalidRequestException extends SushiException {
    public InvalidRequestException(UUID orderId) {
        super(SushiServingStatus.INVALID, orderId);
    }
}
