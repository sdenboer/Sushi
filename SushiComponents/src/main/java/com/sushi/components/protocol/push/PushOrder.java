package com.sushi.components.protocol.push;

import com.sushi.components.message.order.Host;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.FilePayload;
import com.sushi.components.message.wrappers.HasPayload;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PushOrder extends Order implements HasPayload<FilePayload> {

    private static final OrderMethod METHOD = OrderMethod.PUSH;

    private final String dir;
    private final String fileName;
    private final String encryption;
    private final ContentType contentType;
    private final long fileSize;
    private final FilePayload payload;

    @Builder
    public PushOrder(String host, int port, UUID orderId, String dir, String fileName, String encryption, ContentType contentType, long fileSize, FilePayload payload) {
        super(METHOD, new Host(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.payload = payload;
    }

    @Override
    public void addOptionalWrappers() {
        addWrapper(WrapperField.DIR, dir);
        addWrapper(WrapperField.FILE, fileName);
        addWrapper(WrapperField.ENCRYPTION, encryption);
        addWrapper(WrapperField.CONTENT, contentType.getType());
        addWrapper(WrapperField.CONTENT_LENGTH, String.valueOf(fileSize));
    }

    @Override
    public FilePayload getPayload() {
        return payload;
    }
}
