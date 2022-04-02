package com.sushi.components.common.pull;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderMethod;
import com.sushi.components.common.order.SushiOrderWrapper;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

import static com.sushi.components.common.order.SushiOrderWrapperField.*;

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
    public Set<SushiOrderWrapper> optionalSushiWrappers() {
        return Set.of(
                new SushiOrderWrapper(DIR, dir),
                new SushiOrderWrapper(FILE, fileName),
                new SushiOrderWrapper(ENCRYPTION, encryption)
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
}
