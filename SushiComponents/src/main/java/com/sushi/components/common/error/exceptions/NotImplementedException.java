package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.ServingStatus;

import java.util.UUID;

public class NotImplementedException extends SushiException {
    public NotImplementedException(UUID orderId) {
        super(ServingStatus.NOT_IMPLEMENTED, orderId);
    }
}
