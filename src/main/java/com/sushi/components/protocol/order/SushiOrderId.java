package com.sushi.components.protocol.order;

import java.util.UUID;

public record SushiOrderId(UUID orderId) {

    public UUID getOrderId() {
        return orderId;
    }
}
