package com.sushi.components.protocol.pull;

import com.sushi.components.message.order.Host;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PullOrder extends Order {

    private final String dir;
    private final String fileName;
    private final String encryption;

    @Builder
    public PullOrder(String host, int port, UUID orderId, String dir, String fileName,
        String encryption) {
        super(OrderMethod.PULL, new Host(host, port), orderId, null);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
    }

    @Override
    public Map<WrapperField, String> getOptionalWrappers() {
        Map<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.DIR, dir);
        wrappers.put(WrapperField.FILE, fileName);
        return wrappers;
    }

}
