package com.sushi.components.common.message.order;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
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
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.DIR, dir);
        wrappers.put(SushiWrapperField.FILE, fileName);
        return wrappers;
    }

}
