package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.SushiMessage;
import com.sushi.components.common.message.wrappers.HasSushiWrappers;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
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
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.STATUS, String.valueOf(sushiServingStatus.getStatusCode()));
        wrappers.put(SushiWrapperField.ORDER_ID, String.valueOf(orderId));
        return wrappers;
    }

    public SushiServingStatus getSushiServingStatus() {
        return sushiServingStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
