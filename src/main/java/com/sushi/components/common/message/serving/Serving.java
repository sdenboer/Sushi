package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.Message;
import com.sushi.components.common.message.wrappers.HasWrappers;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Serving implements Message, HasWrappers {

    private final ServingStatus servingStatus;
    private final UUID orderId;

    protected Serving(ServingStatus servingStatus, UUID orderId) {
        this.orderId = orderId;
        this.servingStatus = servingStatus;
    }

    @Override
    public Map<WrapperField, String> mandatorySushiWrappers() {
        EnumMap<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.STATUS, String.valueOf(servingStatus.getStatusCode()));
        wrappers.put(WrapperField.ORDER_ID, String.valueOf(orderId));
        return wrappers;
    }

    public ServingStatus getSushiServingStatus() {
        return servingStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
