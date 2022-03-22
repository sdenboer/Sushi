package com.sushi.components.protocol.order;

import com.sushi.components.protocol.SushiHost;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class SushiOrder {

    private final SushiOrderMethod method;
    private final SushiHost host;
    private final UUID orderId;

    public SushiOrder(SushiOrderMethod method,
                      SushiHost host,
                      UUID orderId) {

        this.method = method;
        this.host = host;
        this.orderId = orderId;
    }

    public abstract Set<SushiOrderWrapper> optionalSushiOrderWrappers();


    public String toRequest() {
        return """
                %s
                %s
                                    
                """.formatted(
                sushiOrderWrappersToRequest(mandatoryOrderWrappers()),
                sushiOrderWrappersToRequest(optionalSushiOrderWrappers())
        );
    }

    private Set<SushiOrderWrapper> mandatoryOrderWrappers() {
        return Set.of(new SushiOrderWrapper(SushiOrderWrapperField.METHOD, method.getValue()),
                new SushiOrderWrapper(SushiOrderWrapperField.HOST, host.host()),
                new SushiOrderWrapper(SushiOrderWrapperField.PORT, String.valueOf(host.port())),
                new SushiOrderWrapper(SushiOrderWrapperField.ORDER_ID, String.valueOf(orderId))
        );
    }

    private String sushiOrderWrappersToRequest(Set<SushiOrderWrapper> sushiOrderWrappers) {
        return sushiOrderWrappers.stream()
                .map(wrapper -> wrapper.key().getValue() + ": " + wrapper.value())
                .collect(Collectors.joining("\n"));
    }

    public SushiOrderMethod getMethod() {
        return method;
    }

    public SushiHost getHost() {
        return host;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
