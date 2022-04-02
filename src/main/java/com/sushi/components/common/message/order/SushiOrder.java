package com.sushi.components.common.message.order;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.SushiMessage;
import com.sushi.components.common.message.wrappers.HasSushiWrappers;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Getter;

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
        return Map.of(
                SushiWrapperField.METHOD, method.getValue(),
                SushiWrapperField.HOST, host.host(),
                SushiWrapperField.PORT, String.valueOf(host.port()),
                SushiWrapperField.ORDER_ID, String.valueOf(orderId)
        );
    }

}
