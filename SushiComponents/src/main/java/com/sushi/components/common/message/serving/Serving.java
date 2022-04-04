package com.sushi.components.common.message.serving;

import com.sushi.components.common.message.Message;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Serving extends Message {

    private final ServingStatus servingStatus;
    private final UUID orderId;

    protected Serving(ServingStatus servingStatus, UUID orderId) {
        this.orderId = orderId;
        this.servingStatus = servingStatus;
    }

    @Override
    public void addMandatoryWrappers() {
        addWrapper(WrapperField.STATUS, String.valueOf(servingStatus.getStatusCode()));
        addWrapper(WrapperField.ORDER_ID, String.valueOf(orderId));
    }

    public UUID getOrderId() {
        return orderId;
    }
}
