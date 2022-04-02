package com.sushi.components.common.push;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderMethod;
import com.sushi.components.common.order.SushiOrderWrapper;
import com.sushi.components.common.order.SushiOrderWrapperField;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

public class SushiPushOrder extends SushiOrder {


    private static final SushiOrderMethod METHOD = SushiOrderMethod.PUSH;

    private final String dir;
    private final String fileName;
    private final String encryption;
    private final String content;
    private final long fileSize;

    @Builder
    public SushiPushOrder(String host, int port, UUID orderId, String dir, String fileName, String encryption, String content, long fileSize) {
        super(METHOD, new SushiHost(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
    }


    @Override
    public Set<SushiOrderWrapper> optionalSushiWrappers() {
        return Set.of(
                new SushiOrderWrapper(SushiOrderWrapperField.DIR, dir),
                new SushiOrderWrapper(SushiOrderWrapperField.FILE, fileName),
                new SushiOrderWrapper(SushiOrderWrapperField.ENCRYPTION, encryption),
                new SushiOrderWrapper(SushiOrderWrapperField.CONTENT, content),
                new SushiOrderWrapper(SushiOrderWrapperField.FILE_SIZE, String.valueOf(fileSize))
        );
    }


    public String getDir() {
        return dir;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEncryption() {
        return encryption;
    }

    public String getContent() {
        return content;
    }

    public long getFileSize() {
        return fileSize;
    }

}
