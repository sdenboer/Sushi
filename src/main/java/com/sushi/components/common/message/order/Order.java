package com.sushi.components.common.message.order;

import com.sushi.components.common.message.Message;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Order extends Message {

    private final OrderMethod method;
    private final Host host;
    private final UUID orderId;

    protected Order(OrderMethod method, Host host, UUID orderId) {
        this.orderId = orderId;
        this.method = method;
        this.host = host;
    }

    @Override
    public void addMandatoryWrappers() {
        addWrapper(WrapperField.METHOD, method.getValue());
        addWrapper(WrapperField.HOST, host.host());
        addWrapper(WrapperField.PORT, String.valueOf(host.port()));
        addWrapper(WrapperField.ORDER_ID, String.valueOf(orderId));
    }

}
