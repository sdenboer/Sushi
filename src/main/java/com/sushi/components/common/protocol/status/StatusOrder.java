package com.sushi.components.common.protocol.status;

import com.sushi.components.common.message.order.Host;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.order.OrderMethod;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class StatusOrder extends Order {

    private static final OrderMethod method = OrderMethod.STATUS;

    @Builder
    public StatusOrder(String host, int port, UUID orderId) {
        super(method, new Host(host, port), orderId);
    }

    @Override
    public void addOptionalWrappers() {
        //not needed
    }
}
