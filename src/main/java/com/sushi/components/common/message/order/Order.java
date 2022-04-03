package com.sushi.components.common.message.order;

import com.sushi.components.common.message.Message;
import com.sushi.components.common.message.wrappers.HasWrappers;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Order implements Message, HasWrappers {

    private final OrderMethod method;
    private final Host host;
    private final UUID orderId;

    protected Order(OrderMethod method, Host host, UUID orderId) {
        this.orderId = orderId;
        this.method = method;
        this.host = host;
    }

    @Override
    public Map<WrapperField, String> mandatoryWrappers() {
        EnumMap<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.METHOD, method.getValue());
        wrappers.put(WrapperField.HOST, host.host());
        wrappers.put(WrapperField.PORT, String.valueOf(host.port()));
        wrappers.put(WrapperField.ORDER_ID, String.valueOf(orderId));
        return wrappers;
    }

}
