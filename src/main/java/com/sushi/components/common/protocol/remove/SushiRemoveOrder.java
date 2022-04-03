package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.message.order.SushiHost;
import com.sushi.components.common.message.order.SushiOrder;
import com.sushi.components.common.message.order.SushiOrderMethod;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class SushiRemoveOrder extends SushiOrder {

    private static final SushiOrderMethod method = SushiOrderMethod.REMOVE;
    private final String dir;
    private final String fileName;

    @Builder
    public SushiRemoveOrder(String host, int port, UUID orderId, String dir, String fileName) {
        super(method, new SushiHost(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.DIR, dir);
        wrappers.put(SushiWrapperField.FILE, fileName);
        return wrappers;
    }
}
