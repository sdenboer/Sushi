package com.sushi.components.common.message.order;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.message.wrappers.HasPayload;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.Getter;

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
        return Map.of(
                SushiWrapperField.DIR, dir,
                SushiWrapperField.FILE, fileName,
                SushiWrapperField.ENCRYPTION, encryption,
                SushiWrapperField.CONTENT, content,
                SushiWrapperField.FILE_SIZE, String.valueOf(fileSize)
        );
    }

    @Override
    public FilePayload getPayload() {
        return payload;
    }
}
