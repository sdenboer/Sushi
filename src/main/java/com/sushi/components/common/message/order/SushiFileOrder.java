package com.sushi.components.common.message.order;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

public class SushiFileOrder extends SushiOrder {

    private static final SushiOrderMethod method = SushiOrderMethod.FILE;

    private final String dir;
    private final String fileName;

    @Builder
    public SushiFileOrder(String host, int port, UUID orderId, String dir, String fileName) {
        super(method, new SushiHost(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        return Map.of(
                SushiWrapperField.DIR, dir,
                SushiWrapperField.FILE, fileName
        );
    }

    public String getDir() {
        return dir;
    }

    public String getFileName() {
        return fileName;
    }

}
