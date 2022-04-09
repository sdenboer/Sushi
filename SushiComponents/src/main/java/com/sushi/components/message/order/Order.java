package com.sushi.components.message.order;

import com.sushi.components.message.Message;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Order implements Message {

    protected final OrderMethod orderMethod;
    protected final Host host;
    protected final UUID orderId;
    protected final PayloadContext payloadContext;

    protected Order(OrderMethod orderMethod, Host host, UUID orderId,
                    PayloadContext payloadContext) {
        this.orderId = orderId;
        this.orderMethod = orderMethod;
        this.host = host;
        this.payloadContext = payloadContext;
    }

    @Override
    public Map<WrapperField, String> getWrappers() {
        Map<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.METHOD, orderMethod.getValue());
        wrappers.put(WrapperField.HOST, host.host());
        wrappers.put(WrapperField.PORT, String.valueOf(host.port()));
        wrappers.put(WrapperField.ORDER_ID, String.valueOf(orderId));
        wrappers.putAll(getOptionalWrappers());
        return wrappers;
    }

    public abstract Map<WrapperField, String> getOptionalWrappers();

}
