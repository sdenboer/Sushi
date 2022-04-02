package com.sushi.components.common.file;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderMethod;
import com.sushi.components.common.order.SushiOrderWrapper;
import com.sushi.components.common.order.SushiOrderWrapperField;
import lombok.Builder;

import java.util.Set;
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
    public Set<SushiOrderWrapper> optionalSushiWrappers() {
        return Set.of(
                new SushiOrderWrapper(SushiOrderWrapperField.DIR, dir),
                new SushiOrderWrapper(SushiOrderWrapperField.FILE, fileName)
        );
    }

    public String getDir() {
        return dir;
    }

    public String getFileName() {
        return fileName;
    }

}
