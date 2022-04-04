package com.sushi.components.error.exceptions;

import com.sushi.components.message.serving.ServingStatus;

import java.util.UUID;

public class NotImplementedException extends SushiException {
    public NotImplementedException(UUID orderId) {
        super(ServingStatus.NOT_IMPLEMENTED, orderId);
    }
}
