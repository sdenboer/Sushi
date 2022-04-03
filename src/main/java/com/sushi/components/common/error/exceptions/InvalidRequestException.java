package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.ServingStatus;

import java.util.UUID;

public class InvalidRequestException extends SushiException {
    public InvalidRequestException(UUID orderId) {
        super(ServingStatus.INVALID, orderId);
    }
}
