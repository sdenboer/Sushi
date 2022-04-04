package com.sushi.components.protocol.remove;

import com.sushi.components.message.order.Host;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RemoveOrder extends Order {

    private static final OrderMethod method = OrderMethod.REMOVE;
    private final String dir;
    private final String fileName;

    @Builder
    public RemoveOrder(String host, int port, UUID orderId, String dir, String fileName) {
        super(method, new Host(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
    }

    @Override
    public void addOptionalWrappers() {
        addWrapper(WrapperField.DIR, dir);
        addWrapper(WrapperField.FILE, fileName);
    }
}
