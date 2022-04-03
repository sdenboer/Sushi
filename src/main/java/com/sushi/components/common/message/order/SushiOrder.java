package com.sushi.components.common.message.order;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.SushiMessage;
import com.sushi.components.common.message.wrappers.HasSushiWrappers;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class SushiOrder implements SushiMessage, HasSushiWrappers {

    private final SushiOrderMethod method;
    private final SushiHost host;
    private final UUID orderId;

    protected SushiOrder(SushiOrderMethod method, SushiHost host, UUID orderId) {
        this.orderId = orderId;
        this.method = method;
        this.host = host;
    }

    @Override
    public Map<SushiWrapperField, String> mandatorySushiWrappers() {
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.METHOD, method.getValue());
        wrappers.put(SushiWrapperField.HOST, host.host());
        wrappers.put(SushiWrapperField.PORT, String.valueOf(host.port()));
        wrappers.put(SushiWrapperField.ORDER_ID, String.valueOf(orderId));
        return wrappers;
    }

}
