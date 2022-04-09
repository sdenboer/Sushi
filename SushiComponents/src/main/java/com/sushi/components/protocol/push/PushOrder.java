package com.sushi.components.protocol.push;

import com.sushi.components.message.order.Host;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.PayloadMetaData;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PushOrder extends Order {

    private final String dir;
    private final String fileName;

    @Builder
    public PushOrder(String host, int port, UUID orderId, String dir, String fileName,
                     PayloadContext payloadContext) {
        super(OrderMethod.PUSH, new Host(host, port), orderId, payloadContext);
        this.dir = dir;
        this.fileName = fileName;
    }

    @Override
    public Map<WrapperField, String> getOptionalWrappers() {
        Map<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        PayloadMetaData metaData = payloadContext.payloadMetaData();
        wrappers.put(WrapperField.DIR, dir);
        wrappers.put(WrapperField.FILE, fileName);
        wrappers.put(WrapperField.CONTENT, metaData.contentType().getType());
        wrappers.put(WrapperField.CONTENT_LENGTH, String.valueOf(metaData.contentLength()));
        return wrappers;
    }
}
