package com.sushi.components.common.protocol.file;

import com.sushi.components.common.message.order.Host;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.order.OrderMethod;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class FileOrder extends Order {

    private static final OrderMethod method = OrderMethod.FILE;

    private final String dir;
    private final String fileName;

    @Builder
    public FileOrder(String host, int port, UUID orderId, String dir, String fileName) {
        super(method, new Host(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
    }

    @Override
    public Map<WrapperField, String> optionalSushiWrappers() {
        EnumMap<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.DIR, dir);
        wrappers.put(WrapperField.FILE, fileName);
        return wrappers;
    }

}
