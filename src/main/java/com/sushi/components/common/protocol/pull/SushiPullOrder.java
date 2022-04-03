package com.sushi.components.common.protocol.pull;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.order.SushiOrder;
import com.sushi.components.common.message.order.SushiOrderMethod;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.ENCRYPTION;

@Getter
public class SushiPullOrder extends SushiOrder {

    private static final SushiOrderMethod METHOD = SushiOrderMethod.PULL;
    private final String dir;
    private final String fileName;
    private final String encryption;

    @Builder
    public SushiPullOrder(String host, int port, UUID orderId, String dir, String fileName, String encryption) {
        super(METHOD, new SushiHost(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
    }

    @Override
    public Map<SushiWrapperField, String> optionalSushiWrappers() {
        EnumMap<SushiWrapperField, String> wrappers = new EnumMap<>(SushiWrapperField.class);
        wrappers.put(SushiWrapperField.DIR, dir);
        wrappers.put(SushiWrapperField.FILE, fileName);
        wrappers.put(ENCRYPTION, encryption);
        return wrappers;
    }

}
