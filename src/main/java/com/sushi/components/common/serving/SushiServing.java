package com.sushi.components.common.serving;

import com.sushi.components.common.SushiMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

public abstract class SushiServing implements SushiMessage<SushiServingWrapper> {

    private final SushiServingStatus sushiServingStatus;

    public SushiServing(SushiServingStatus sushiServingStatus) {
        this.sushiServingStatus = sushiServingStatus;
    }

    @Override
    public Set<SushiServingWrapper> mandatorySushiWrappers() {
        return Set.of(new SushiServingWrapper(SushiServingWrapperField.STATUS, String.valueOf(sushiServingStatus.getStatusCode())));
    }

    public static Map<SushiServingWrapperField, String> mapToHeaders(String request) {
        String[] splitRequest = request.split("\n");

        return Arrays.stream(splitRequest)
                .takeWhile(not(String::isEmpty))
                .map(l -> l.split(": "))
                .collect(toMap(h -> SushiServingWrapperField.fromString(h[0]), h -> h[1]));
    }

    public SushiServingStatus getSushiServingStatus() {
        return sushiServingStatus;
    }

}
