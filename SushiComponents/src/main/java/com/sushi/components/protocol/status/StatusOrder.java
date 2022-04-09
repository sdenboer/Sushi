package com.sushi.components.protocol.status;

import com.sushi.components.message.order.Host;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Getter
public class StatusOrder extends Order {

    @Builder
    public StatusOrder(String host, int port, UUID orderId) {
        super(OrderMethod.STATUS, new Host(host, port), orderId, null);
    }

    @Override
    public Map<WrapperField, String> getOptionalWrappers() {
        return Collections.emptyMap();
    }
}
