package com.sushi.components.common.protocol.status;

import com.sushi.components.common.message.order.Host;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.order.OrderMethod;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Getter
public class StatusOrder extends Order {

    private static final OrderMethod method = OrderMethod.STATUS;

    @Builder
    public StatusOrder(String host, int port, UUID orderId) {
        super(method, new Host(host, port), orderId);
    }

    @Override
    public @NonNull Map<WrapperField, String> optionalWrappers() {
        return Collections.emptyMap();
    }
}
