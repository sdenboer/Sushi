package com.sushi.components.common.pull;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderMethod;
import com.sushi.components.common.order.SushiOrderWrapper;
import com.sushi.components.common.order.SushiOrderWrapperField;
import lombok.Builder;

import java.util.Map;
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

    public static SushiPullOrder fromRequest(String request) {
        Map<SushiOrderWrapperField, String> wrappers = SushiOrder.mapToHeaders(request);

        return SushiPullOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .encryption(wrappers.get(ENCRYPTION))
                .build();
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
