package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.SushiMessage;
import com.sushi.components.common.message.wrappers.HasSushiWrappers;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public abstract class SushiServing implements SushiMessage, HasSushiWrappers {

    private final SushiServingStatus sushiServingStatus;
    private final UUID orderId;

    protected SushiServing(SushiServingStatus sushiServingStatus, UUID orderId) {
        this.orderId = orderId;
        this.sushiServingStatus = sushiServingStatus;
    }

    @Override
    public Map<SushiWrapperField, String> mandatorySushiWrappers() {
        return Map.of(
                SushiWrapperField.STATUS, String.valueOf(sushiServingStatus.getStatusCode()),
                SushiWrapperField.ORDER_ID, String.valueOf(orderId)
        );
    }

    public SushiServingStatus getSushiServingStatus() {
        return sushiServingStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
