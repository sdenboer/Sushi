package com.sushi.components.common.protocol.push;

import com.sushi.components.common.message.order.Host;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.order.OrderMethod;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PushOrder extends Order implements HasPayload<FilePayload> {

    private static final OrderMethod METHOD = OrderMethod.PUSH;

    private final String dir;
    private final String fileName;
    private final String encryption;
    private final String content;
    private final long fileSize;
    private final FilePayload payload;

    @Builder
    public PushOrder(String host, int port, UUID orderId, String dir, String fileName, String encryption, String content, long fileSize, FilePayload payload) {
        super(METHOD, new Host(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
        this.payload = payload;
    }

    @Override
    public Map<WrapperField, String> optionalSushiWrappers() {
        EnumMap<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.DIR, dir);
        wrappers.put(WrapperField.FILE, fileName);
        wrappers.put(WrapperField.ENCRYPTION, encryption);
        wrappers.put(WrapperField.CONTENT, content);
        wrappers.put(WrapperField.CONTENT_LENGTH, String.valueOf(fileSize));
        return wrappers;
    }

    @Override
    public FilePayload getPayload() {
        return payload;
    }
}
