package com.sushi.components.error.exceptions;

import com.sushi.components.message.serving.ServingStatus;

import java.util.UUID;

public class NotFoundException extends SushiException {
    public NotFoundException(Exception e, UUID orderId) {
        super(ServingStatus.NOT_FOUND, orderId);
        e.printStackTrace();
    }
}
