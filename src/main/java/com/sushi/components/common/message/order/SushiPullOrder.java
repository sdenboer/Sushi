package com.sushi.components.common.message.order;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.message.wrappers.SushiWrapperField;
import lombok.Builder;
import lombok.Getter;

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
        return Map.of(
                SushiWrapperField.DIR, dir,
                SushiWrapperField.FILE, fileName,
                ENCRYPTION, encryption
        );
    }

}
