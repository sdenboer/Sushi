package com.sushi.components.protocol.file;

import com.sushi.components.message.order.Host;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class FileOrder extends Order {

    private final String dir;
    private final String fileName;

    @Builder
    public FileOrder(String host, int port, UUID orderId, String dir, String fileName) {
        super(OrderMethod.FILE, new Host(host, port), orderId, null);
        this.dir = dir;
        this.fileName = fileName;
    }

    @Override
    public Map<WrapperField, String> getOptionalWrappers() {
        Map<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.DIR, dir);
        wrappers.put(WrapperField.FILE, fileName);
        return wrappers;
    }
}
