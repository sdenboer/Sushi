package com.sushi.components.common.protocol.pull;

import com.sushi.components.common.message.order.Host;
import com.sushi.components.common.message.order.Order;
import com.sushi.components.common.message.order.OrderMethod;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

import static com.sushi.components.common.message.wrappers.WrapperField.ENCRYPTION;

@Getter
public class PullOrder extends Order {

    private static final OrderMethod METHOD = OrderMethod.PULL;
    private final String dir;
    private final String fileName;
    private final String encryption;

    @Builder
    public PullOrder(String host, int port, UUID orderId, String dir, String fileName, String encryption) {
        super(METHOD, new Host(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
    }

    @Override
    public void addOptionalWrappers() {
        addWrapper(WrapperField.DIR, dir);
        addWrapper(WrapperField.FILE, fileName);
        addWrapper(ENCRYPTION, encryption);
    }

}
