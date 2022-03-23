package com.sushi.components.common.order;

import com.sushi.components.common.SushiHost;
import com.sushi.components.common.SushiMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

public abstract class SushiOrder implements SushiMessage<SushiOrderWrapper> {

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


    @Override
    public Set<SushiOrderWrapper> mandatorySushiWrappers() {
        return Set.of(new SushiOrderWrapper(SushiOrderWrapperField.METHOD, method.getValue()),
                new SushiOrderWrapper(SushiOrderWrapperField.HOST, host.host()),
                new SushiOrderWrapper(SushiOrderWrapperField.PORT, String.valueOf(host.port())),
                new SushiOrderWrapper(SushiOrderWrapperField.ORDER_ID, String.valueOf(orderId))
        );
    }

    public static Map<SushiOrderWrapperField, String> mapToHeaders(String request) {
        String[] splitRequest = request.split("\n");

        return Arrays.stream(splitRequest)
                .takeWhile(not(String::isEmpty))
                .map(l -> l.split(": "))
                .collect(toMap(h -> SushiOrderWrapperField.fromString(h[0]), h -> h[1]));
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
