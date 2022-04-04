package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.ServingStatus;

import java.util.UUID;

public class NotFoundException extends SushiException {
    public NotFoundException(Exception e, UUID orderId) {
        super(ServingStatus.NOT_FOUND, orderId);
        e.printStackTrace();
    }
}
