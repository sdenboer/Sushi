package com.sushi.components.common.protocol.push;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.order.SushiOrder;
import com.sushi.components.common.message.order.SushiOrderMethod;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class SushiPushOrder extends SushiOrder implements HasPayload<FilePayload> {

    private static final SushiOrderMethod METHOD = SushiOrderMethod.PUSH;

    private final String dir;
    private final String fileName;
    private final String encryption;
    private final String content;
    private final long fileSize;
    private final FilePayload payload;

    @Builder
    public SushiPushOrder(String host, int port, UUID orderId, String dir, String fileName, String encryption, String content, long fileSize, FilePayload payload) {
        super(METHOD, new SushiHost(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
        this.payload = payload;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.DIR, dir);
        wrappers.put(SushiWrapperField.FILE, fileName);
        wrappers.put(SushiWrapperField.ENCRYPTION, encryption);
        wrappers.put(SushiWrapperField.CONTENT, content);
        wrappers.put(SushiWrapperField.CONTENT_LENGTH, String.valueOf(fileSize));
        return wrappers;
    }

    @Override
    public FilePayload getPayload() {
        return payload;
    }
}
