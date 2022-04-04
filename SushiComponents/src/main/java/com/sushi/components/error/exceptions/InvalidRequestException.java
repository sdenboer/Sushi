package com.sushi.components.error.exceptions;

import com.sushi.components.message.serving.ServingStatus;

import java.util.UUID;

public class InvalidRequestException extends SushiException {
    public InvalidRequestException(UUID orderId) {
        super(ServingStatus.INVALID, orderId);
    }
}
