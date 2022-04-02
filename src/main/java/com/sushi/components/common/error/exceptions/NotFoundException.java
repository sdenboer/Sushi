package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.SushiServingStatus;

import java.util.UUID;

public class NotFoundException extends SushiException {
    public NotFoundException(Exception e, UUID orderId) {
        super(SushiServingStatus.NOT_FOUND, orderId);
        e.printStackTrace();
    }
}
