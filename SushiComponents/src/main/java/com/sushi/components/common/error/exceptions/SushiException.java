package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.ServingStatus;

import java.util.UUID;

public abstract class SushiException extends RuntimeException {

    private final ServingStatus status;
    private final UUID orderId;

    protected SushiException(ServingStatus status, UUID orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    public ServingStatus getStatus() {
        return status;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
