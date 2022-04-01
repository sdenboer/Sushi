package com.sushi.components.common.error;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public abstract class SushiException extends RuntimeException {

    private final SushiServingStatus status;
    private final UUID orderId;

    protected SushiException(SushiServingStatus status, UUID orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    public SushiServingStatus getStatus() {
        return status;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
